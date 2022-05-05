package object.store.postgresservice.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.exceptions.CreateObjectFailed;
import object.store.postgresservice.exceptions.ObjectNotFound;
import object.store.postgresservice.services.AdditionalPropertyService;
import object.store.postgresservice.services.builders.SQLStatementBuilder;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.util.Strings;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
public record UtilsService(SQLUtils sqlUtils, SQLStatementBuilder sqlBuilder,
                           AdditionalPropertyService additionalPropertyService, DatabaseClient client) {

  public Mono<Map<String, Object>> insertObject(Mono<Tuple2<Map<String, Object>,
      TypeDto>> monoPair) {
    return monoPair.flatMap(pair -> {
      Map<String, Object> caseSensitiveObject = new HashMap<>(pair.getT1());
      TypeDto type = pair.getT2();
      final String primaryKey = getPrimaryKeyName(type);
      if (type.getAdditionalProperties()) {
        additionalPropertyService.mapToAdditionalProperties(caseSensitiveObject, type);
      }
      return client.sql(sqlBuilder.insertObject(caseSensitiveObject, type.getName()).getStatement())
          .fetch().rowsUpdated()
          .flatMap(rows -> {
            if (Objects.equals(rows, 0)) {
              return Mono.error(new CreateObjectFailed(caseSensitiveObject.toString(), type.toString()));
            }
            return Mono.just(rows);
          })
          .map(t -> Triple.of(type.getName(), caseSensitiveObject.get(primaryKey).toString(), primaryKey));
    }).flatMap(triple -> getSingleSelectResult(triple.getLeft(),
        triple.getRight(),
        triple.getMiddle()));
  }

  public String getPrimaryKeyName(TypeDto type) {
    Optional<BasicBackendDefinitionDto> primary =
        type.getBackendKeyDefinitions().stream()
            .filter(definition -> BackendKeyType.PRIMARYKEY.equals(definition.getType()))
            .findFirst();
    return primary
        .map(BasicBackendDefinitionDto::getKey)
        .orElse(Strings.EMPTY);
  }

  public Mono<Map<String, Object>> getSingleSelectResult( String type, String primaryKey,
      String primaryValue) {
    return client.sql(sqlBuilder.selectObjectByPrimary(type, primaryKey, primaryValue).getStatement())
        .fetch()
        .first()
        .map(sqlUtils::mapJsonObjects)
        .map(additionalPropertyService::mapAdditionalProperties)
        .switchIfEmpty(Mono.error(new ObjectNotFound(primaryValue, type)));
  }
}
