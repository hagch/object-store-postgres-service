package object.store.postgresservice.daos;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyDefinition;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.gen.dbservice.models.Type;
import object.store.postgresservice.builders.SQLStatementBuilder;
import object.store.postgresservice.services.AdditionalPropertyService;
import object.store.postgresservice.utils.SQLUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

@Service
public record ObjectsDao(DatabaseClient client, R2dbcEntityTemplate template, SQLStatementBuilder sqlBuilder,
                         SQLUtils sqlUtils, AdditionalPropertyService additionalPropertyService) {

  public Mono<Map<String, Object>> insertObject(Mono<Tuple2<Map<String, Object>, Type>> monoPair) {
    return monoPair.flatMap(pair -> {
      Map<String, Object> caseSensitiveObject = new HashMap<>(pair.getT1());
      Type type = pair.getT2();
      final String primaryKey = getPrimaryKeyName(type);
      String uuid = UUID.randomUUID().toString();
      caseSensitiveObject.put(primaryKey, uuid);
      if (type.getAdditionalProperties()) {
        additionalPropertyService.mapToAdditionalProperties(caseSensitiveObject, type);
      }
      return client.sql(sqlBuilder.insertObject(caseSensitiveObject, type.getName()).getStatement())
          .fetch().rowsUpdated().map(t -> Triple.of(type.getName(), uuid, primaryKey));
    }).flatMap(triple -> getSingleSelectResult(triple.getLeft(), triple.getRight(), triple.getMiddle()));
  }

  public Mono<Map<String, Object>> getObject(Mono<Tuple2<String, Type>> monoPair) {
    return monoPair.flatMap(pair -> {
      String objectId = pair.getT1();
      Type type = pair.getT2();
      final String primaryKey = getPrimaryKeyName(type);
      return getSingleSelectResult(type.getName(), primaryKey, objectId);
    });
  }

  public Flux<Map<String, Object>> getObjects(Mono<Type> typeMono) {
    return typeMono
        .flatMapMany(
            type -> client.sql(sqlBuilder.selectObjectsByTableName(type.getName()).getStatement()).fetch().all())
        .map(sqlUtils::mapJsonObjects)
        .map(additionalPropertyService::mapAdditionalProperties);

  }

  public Mono<Map<String, Object>> updateObject(
      Mono<Tuple3<Map<String, Object>, Map<String, Object>, Type>> tripleMono) {
    return tripleMono.flatMap(triple -> {
      Map<String, Object> newObject = triple.getT1();
      Map<String, Object> oldObject = triple.getT2();
      Type type = triple.getT3();
      String primaryKey = getPrimaryKeyName(type);
      String primaryValue = Objects.toString(newObject.get(primaryKey));
      MapDifference<String, Object> difference = Maps.difference(newObject, oldObject);
      Map<String, Object> differences = new HashMap<>(newObject);
      differences.keySet().removeAll(difference.entriesInCommon().keySet());
      Mono<Map<String, Object>> monoObject = Mono.just(oldObject);
      differences.remove(primaryKey);
      differences = additionalPropertyService.mergeDifferencesWithAdditionalPropertyValues(differences,
          additionalPropertyService.getAdditionalProperties(newObject, type));
      additionalPropertyService.mapToAdditionalProperties(differences, type);
      if (!differences.isEmpty()) {
        monoObject = client.sql(
                sqlBuilder.updateObjectByPrimaryKey(type.getName(), primaryKey, primaryValue, differences).getStatement())
            .fetch().rowsUpdated()
            .flatMap(n -> client.sql(
                    sqlBuilder.selectObjectByPrimary(type.getName(), primaryKey, primaryValue).getStatement()).fetch()
                .first());
      }
      return monoObject.map(sqlUtils::mapJsonObjects)
          .map(additionalPropertyService::mapAdditionalProperties);
    });
  }

  private String getPrimaryKeyName(Type type) {
    Optional<BackendKeyDefinition> primary =
        type.getBackendKeyDefinitions().stream()
            .filter(definition -> BackendKeyType.PRIMARYKEY.equals(definition.getType()))
            .findFirst();
    return primary
        .map(BackendKeyDefinition::getKey)
        .orElse(Strings.EMPTY);
  }

  private Mono<Map<String, Object>> getSingleSelectResult(String type, String primaryKey, String primaryValue) {
    return client.sql(sqlBuilder.selectObjectByPrimary(type, primaryKey, primaryValue).getStatement())
        .fetch()
        .first()
        .map(sqlUtils::mapJsonObjects)
        .map(additionalPropertyService::mapAdditionalProperties).log();
  }
}

