package object.store.postgresservice.daos;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.exceptions.CreateObjectFailed;
import object.store.postgresservice.exceptions.DeleteObjectFailed;
import object.store.postgresservice.exceptions.GetAllObjectsByTypeNameFailed;
import object.store.postgresservice.exceptions.ObjectNotFound;
import object.store.postgresservice.exceptions.UpdateObjectFailed;
import object.store.postgresservice.services.AdditionalPropertyService;
import object.store.postgresservice.services.TypeService;
import object.store.postgresservice.services.builders.SQLStatementBuilder;
import object.store.postgresservice.utils.SQLUtils;
import object.store.postgresservice.utils.UtilsService;
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
public record ObjectsDao(R2dbcEntityTemplate template, UtilsService utilsService) {
  public Mono<Map<String, Object>> insertObject(Mono<Tuple2<Map<String, Object>, TypeDto>> monoPair) {
    return utilsService.insertObject(monoPair);
  }

  public Mono<Map<String, Object>> getObject(Mono<Tuple2<String, TypeDto>> monoPair) {
    return monoPair.flatMap(pair -> {
      String objectId = pair.getT1();
      TypeDto type = pair.getT2();
      final String primaryKey = utilsService.getPrimaryKeyName(type);
      return utilsService.getSingleSelectResult(type.getName(),
          primaryKey, objectId);
    });
  }

  public Flux<Map<String, Object>> getObjects(Mono<TypeDto> typeMono) {
    return typeMono
        .flatMapMany(
            type -> utilsService.client().sql(utilsService.sqlBuilder().selectObjectsByTableName(type.getName()).getStatement()).fetch().all()
                .switchIfEmpty(Mono.error(new GetAllObjectsByTypeNameFailed(type.getName())))
        )
        .map(utilsService.sqlUtils()::mapJsonObjects)
        .map(utilsService.additionalPropertyService()::mapAdditionalProperties);

  }

  public Mono<Map<String, Object>> updateObject(
      Mono<Tuple3<Map<String, Object>, Map<String, Object>, TypeDto>> tripleMono) {
    return tripleMono.flatMap(triple -> {
      Map<String, Object> newObject = triple.getT1();
      Map<String, Object> oldObject = triple.getT2();
      TypeDto type = triple.getT3();
      String primaryKey = utilsService.getPrimaryKeyName(type);
      String primaryValue = Objects.toString(newObject.get(primaryKey));
      MapDifference<String, Object> difference = Maps.difference(newObject, oldObject);
      Map<String, Object> differences = new HashMap<>(newObject);
      differences.keySet().removeAll(difference.entriesInCommon().keySet());
      Mono<Map<String, Object>> monoObject = Mono.just(oldObject);
      differences.remove(primaryKey);
      differences = utilsService.additionalPropertyService().mergeDifferencesWithAdditionalPropertyValues(differences,
          utilsService.additionalPropertyService().getAdditionalProperties(newObject, type));
      utilsService.additionalPropertyService().mapToAdditionalProperties(differences, type);
      if (!differences.isEmpty()) {
        monoObject = utilsService.client().sql(
                utilsService.sqlBuilder().updateObjectByPrimaryKey(type.getName(), primaryKey, primaryValue,
                    differences).getStatement())
            .fetch().rowsUpdated()
            .flatMap(rows -> {
              if (Objects.equals(rows, 0)) {
                return Mono.error(new UpdateObjectFailed(newObject.toString(),type.toString()));
              }
              return Mono.just(rows);
            })
            .flatMap(n -> utilsService.client().sql(
                    utilsService.sqlBuilder().selectObjectByPrimary(type.getName(), primaryKey, primaryValue).getStatement()).fetch()
                .first());
      }
      return monoObject.map(utilsService.sqlUtils()::mapJsonObjects)
          .map(utilsService.additionalPropertyService()::mapAdditionalProperties);
    });
  }

  public Mono<Integer> deleteObject(String objectId, TypeDto typeDto){
      return utilsService.client().sql(utilsService.sqlBuilder().deleteObjectByPrimaryKey(typeDto.getName(),
              utilsService().getPrimaryKeyName(typeDto),objectId).getStatement())
          .fetch().rowsUpdated().flatMap(rows -> {
            if (Objects.equals(rows, 0)) {
              return Mono.error(new DeleteObjectFailed(objectId));
            }
            return Mono.just(rows);
          });
  }
}

