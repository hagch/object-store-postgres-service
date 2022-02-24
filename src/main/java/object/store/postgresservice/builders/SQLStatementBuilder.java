package object.store.postgresservice.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import object.store.postgresservice.builders.sql.statement.SQLCreateTableBuilder;
import object.store.postgresservice.builders.sql.statement.SQLInsertObjectBuilder;
import object.store.postgresservice.builders.sql.statement.SQLStatement;
import object.store.postgresservice.dtos.BackendKeyDefinitionDto;
import object.store.postgresservice.dtos.TypeDto;
import org.apache.logging.log4j.util.Strings;
import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.BooleanSchema;
import org.everit.json.schema.NumberSchema;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.stereotype.Service;

@Service
public record SQLStatementBuilder(ObjectMapper mapper) {

  public SQLStatement createTable(TypeDto type){
    SQLCreateTableBuilder builder = SQLStatement.createTableBuilder().name(type.getName());
    for (BackendKeyDefinitionDto keyDefinition : type.getBackendKeyDefinitionDtos()) {
      String key = keyDefinition.getKey();
      builder = switch (keyDefinition.getType()) {
        case TIMESTAMP, DATE -> builder.fieldTimeStamp(key,true);
        case INTEGER -> builder.fieldInteger(key,true);
        case BOOLEAN -> builder.fieldBoolean(key,true);
        case STRING -> builder.fieldString(key,true);
        case DOUBLE -> builder.fieldDouble(key,true);
        case LONG -> builder.fieldLong(key,true);
        case PRIMARYKEY -> builder.fieldUuid(key,true).primaryKey(key);
        case OBJECT -> {
          builder = builder.jsonValidation("fk_"+key,key,getObjectSchema(keyDefinition.getProperties()));
          yield builder.fieldObject(key,true);
        }
        case ARRAY -> {
          builder = builder.jsonValidation("fk:"+key,key,getArraySchema(keyDefinition));
          yield builder.fieldObject(key, true);
        }
      };
    }
    return builder.build();
  }

  public SQLStatement insertObject(Map<String,Object> object, String typeName){
    SQLInsertObjectBuilder builder = SQLStatement.insertObjectBuilder().name(typeName);
    for (Entry<String, Object> entry : object.entrySet()) {
      if(entry.getValue() instanceof List<?> || entry.getValue() instanceof Map<?,?>){
        try {
          builder.jsonValue(entry.getKey(),entry.getValue());
        } catch (JsonProcessingException e) {
          Logger.getGlobal().warning(e.toString());
          e.printStackTrace();
        }
      }else {
        builder.keyValue(entry.getKey(),entry.getValue());
      }
    }
    return builder.build();
  }

  public SQLStatement selectObjectByPrimary(String tableName,String key, String value){
    return SQLStatement.selectObjectBuilder().name(tableName).byPrimaryKey(key,value).build();
  }

  public SQLStatement selectObjectsByTableName(String tableName){
    return SQLStatement.selectObjectBuilder().name(tableName).build();
  }

  public SQLStatement updateObjectByPrimaryKey(String tableName, String primaryKey, String primaryValue, Map<String,
      Object> newValues){
    var mappedValues = newValues.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> {
      if(entry.getValue() instanceof List<?> || entry.getValue() instanceof Map<?,?>){
        try {
          return mapper.writeValueAsString(entry.getValue());
        } catch (JsonProcessingException e) {
          Logger.getGlobal().warning(e.toString());
          e.printStackTrace();
        }
      }
      return entry.getValue();
    }));
    return SQLStatement.updateObjectBuilder().name(tableName).primaryKey(primaryKey,primaryValue).values(mappedValues).build();
  }

  private Schema getObjectSchema(List<BackendKeyDefinitionDto> properties){
    ObjectSchema.Builder builder = ObjectSchema.builder();

    for (BackendKeyDefinitionDto backendKey : properties) {
      String key = backendKey.getKey();
      builder = switch (backendKey.getType()){
        case TIMESTAMP, DATE, LONG, INTEGER, DOUBLE -> builder.addPropertySchema(key, NumberSchema.builder().build());
        case BOOLEAN -> builder.addPropertySchema(key, BooleanSchema.builder().build());
        case STRING, PRIMARYKEY -> builder.addPropertySchema(key, StringSchema.builder().build());
        case OBJECT -> builder.addPropertySchema(key,getObjectSchema(backendKey.getProperties()));
        case ARRAY -> builder.addPropertySchema(key,getArraySchema(backendKey));
      };
    }

    return builder.build();
  }

  private Schema getArraySchema(BackendKeyDefinitionDto property){
    if (Objects.nonNull(property.getPrimitiveArrayType()) && Objects.nonNull(
        property.getPrimitiveArrayType())) {
      return StringSchema.builder().build();
    } else {
      return ArraySchema.builder().allItemSchema(getObjectSchema(property.getProperties())).build();
    }
  }
}
