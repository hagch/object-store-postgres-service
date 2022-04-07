package object.store.postgresservice.services.builders;

import static object.store.postgresservice.services.AdditionalPropertyService.ADDITIONAL_PROPERTIES_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.ArrayDefinitionDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.dtos.models.ObjectDefinitionDto;
import object.store.postgresservice.dtos.models.RelationDefinitionDto;
import object.store.postgresservice.services.TypeService;
import object.store.postgresservice.services.builders.sql.statement.SQLCreateTableBuilder;
import object.store.postgresservice.services.builders.sql.statement.SQLInsertObjectBuilder;
import object.store.postgresservice.services.builders.sql.statement.SQLStatement;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SQLStatementBuilder {

  private final ObjectMapper mapper;
  private final TypeService typeService;

  public SQLStatementBuilder(ObjectMapper mapper, @Lazy TypeService typeService) {
    this.mapper = mapper;
    this.typeService = typeService;
  }

  public Mono<SQLStatement> createTable(TypeDto type) {
    final SQLCreateTableBuilder builder = SQLStatement.createTableBuilder().name(type.getName());
    if (type.getAdditionalProperties()) {
      builder.fieldObject(ADDITIONAL_PROPERTIES_KEY, true);
    }
    return Flux.fromIterable(type.getBackendKeyDefinitions()).flatMap(definition -> {
      if (Boolean.TRUE.equals(definition.getIsUnique()) && !BackendKeyType.PRIMARYKEY.equals(definition.getType())) {
        builder.uniqueKey(definition.getKey());
      }
      return switch (definition.getType()) {
        case TIMESTAMP, DATE -> Mono.just(builder.fieldTimeStamp(definition.getKey(), definition.getIsNullAble()));
        case INTEGER -> Mono.just(builder.fieldInteger(definition.getKey(), definition.getIsNullAble()));
        case BOOLEAN -> Mono.just(builder.fieldBoolean(definition.getKey(), definition.getIsNullAble()));
        case STRING -> Mono.just(builder.fieldString(definition.getKey(), definition.getIsNullAble()));
        case DOUBLE -> Mono.just(builder.fieldDouble(definition.getKey(), definition.getIsNullAble()));
        case LONG -> Mono.just(builder.fieldLong(definition.getKey(), definition.getIsNullAble()));
        case PRIMARYKEY -> Mono.just(
            builder.fieldUuid(definition.getKey(), definition.getIsNullAble()).primaryKey(definition.getKey()));
        case OBJECT -> {
          builder.jsonValidation("validation_" + definition.getKey(), definition.getKey(),
              getObjectSchema(((ObjectDefinitionDto) definition).getProperties(),
                  ((ObjectDefinitionDto) definition).getAdditionalProperties()));
          yield Mono.just(builder.fieldObject(definition.getKey(), definition.getIsNullAble()));
        }
        case ARRAY -> {
          if (!type.getAdditionalProperties()) {
            builder.jsonValidation("validation_" + definition.getKey(), definition.getKey(),
                getArraySchema((ArrayDefinitionDto) definition,
                    ((ArrayDefinitionDto) definition).getAdditionalProperties()));
          }
          yield Mono.just(builder.fieldObject(definition.getKey(), definition.getIsNullAble()));
        }
        case ONETOMANY, ONETOONE -> typeService.getById(
            UUID.fromString(((RelationDefinitionDto) definition).getReferencedTypeId())).map(referencedType ->
            builder.foreignKey("fk_" + definition.getKey(), definition.getKey(), referencedType.getName(),
                ((RelationDefinitionDto) definition).getReferenceKey()).fieldString(definition.getKey(),
                definition.getIsNullAble()));
      };
    }).collectList().map(b -> builder.build());
  }

  public SQLStatement insertObject(Map<String, Object> object, String typeName) {
    SQLInsertObjectBuilder builder = SQLStatement.insertObjectBuilder().name(typeName);
    for (Entry<String, Object> entry : object.entrySet()) {
      if (entry.getValue() instanceof List<?> || entry.getValue() instanceof Map<?, ?>) {
        try {
          builder.jsonValue(entry.getKey(), entry.getValue());
        } catch (JsonProcessingException e) {
          Logger.getGlobal().warning(e.toString());
          e.printStackTrace();
        }
      } else {
        builder.keyValue(entry.getKey(), entry.getValue());
      }
    }
    return builder.build();
  }

  public SQLStatement selectObjectByPrimary(String tableName, String key, String value) {
    return SQLStatement.selectObjectBuilder().name(tableName).byKey(key, value).build();
  }

  public SQLStatement selectObjectsByTableName(String tableName) {
    return SQLStatement.selectObjectBuilder().name(tableName).build();
  }

  public SQLStatement updateObjectByPrimaryKey(String tableName, String primaryKey, String primaryValue,
      Map<String, Object> newValues) {
    var mappedValues = newValues.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> {
      if (entry.getValue() instanceof List<?> || entry.getValue() instanceof Map<?, ?>) {
        try {
          return mapper.writeValueAsString(entry.getValue());
        } catch (JsonProcessingException e) {
          Logger.getGlobal().warning(e.toString());
          e.printStackTrace();
        }
      }
      return entry.getValue();
    }));
    return SQLStatement.updateObjectBuilder().name(tableName).primaryKey(primaryKey, primaryValue).values(mappedValues)
        .build();
  }

  private Schema getObjectSchema(List<BasicBackendDefinitionDto> properties, boolean additionalProperties) {
    ObjectSchema.Builder builder = ObjectSchema.builder();
    if (additionalProperties) {
      builder.additionalProperties(true);
    }
    for (BasicBackendDefinitionDto backendKey : properties) {
      String key = backendKey.getKey();
      builder = switch (backendKey.getType()) {
        case TIMESTAMP, DATE, LONG, INTEGER, DOUBLE -> builder.addPropertySchema(key, NumberSchema.builder().build());
        case BOOLEAN -> builder.addPropertySchema(key, BooleanSchema.builder().build());
        case STRING, PRIMARYKEY, ONETOONE, ONETOMANY -> builder.addPropertySchema(key, StringSchema.builder().build());
        case OBJECT -> builder.addPropertySchema(key,
            getObjectSchema(((ObjectDefinitionDto) backendKey).getProperties(), additionalProperties));
        case ARRAY -> builder.addPropertySchema(key,
            getArraySchema((ArrayDefinitionDto) backendKey, additionalProperties));
      };
    }
    return builder.build();
  }

  private Schema getArraySchema(ArrayDefinitionDto property, boolean additionalProperties) {
    if (Objects.nonNull(property.getPrimitiveArrayType())) {
      return StringSchema.builder().build();
    } else {
      return ArraySchema.builder().allItemSchema(getObjectSchema(property.getProperties(), additionalProperties))
          .build();
    }
  }
}
