package object.store.postgresservice.services;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyDefinition;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.gen.dbservice.models.Type;
import object.store.postgresservice.daos.ObjectsDao;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record ObjectsService(ObjectsDao objectsDao, TypeService typeService) {

  public Mono<Map<String, Object>> insertObjectByTypeName(Mono<Map<String, Object>> monoObject, String typeName) {
    return objectsDao.insertObject(Mono.zip(monoObject, typeService.getByName(typeName)));
  }

  public Mono<Map<String, Object>> insertObjectByTypId(Mono<Map<String, Object>> monoObject, String typeId) {
    return objectsDao.insertObject(Mono.zip(monoObject, typeService.getById(UUID.fromString(typeId))));
  }

  public Mono<Map<String, Object>> getObjectByTypId(String objectId, String typeId) {
    return objectsDao.getObject(Mono.zip(Mono.just(objectId), typeService.getById(UUID.fromString(typeId))));
  }

  public Mono<Map<String, Object>> getObjectByTypName(String objectId, String typeName) {
    return objectsDao.getObject(Mono.zip(Mono.just(objectId), typeService.getByName(typeName)));
  }

  public Mono<Flux<Map<String, Object>>> getObjectsByTypId(String typeId) {
    return Mono.just(objectsDao.getObjects(typeService.getById(UUID.fromString(typeId))));
  }

  public Mono<Flux<Map<String, Object>>> getObjectsByTypeName(String typeName) {
    return Mono.just(objectsDao.getObjects(typeService.getByName(typeName)));
  }

  public Mono<Map<String, Object>> updateObjectsByTypeId(Mono<Map<String, Object>> monoObject, String typeId) {
    return monoObject.flatMap(object -> Mono.zip(Mono.just(object), typeService.getById(UUID.fromString(typeId))))
        .flatMap(pair -> {
          String primaryKey = getPrimaryKeyName(pair.getT2());
          return objectsDao.getObject(Mono.zip(Mono.just(primaryKey), Mono.just(pair.getT2())))
              .map(oldObject -> Mono.zip(Mono.just(pair.getT1()), Mono.just(oldObject), Mono.just(pair.getT2())));
        })
        .flatMap(objectsDao::updateObject);
  }

  public Mono<Map<String, Object>> updateObjectsByTypeName(Mono<Map<String, Object>> monoObject, String typeName) {
    return monoObject.flatMap(object -> Mono.zip(Mono.just(object), typeService.getByName(typeName)))
        .flatMap(pair -> {
          String primaryKey = getPrimaryKeyName(pair.getT2());
          String objectId = Objects.toString(pair.getT1().get(primaryKey));
          return objectsDao.getObject(Mono.zip(Mono.just(objectId), Mono.just(pair.getT2())))
              .map(oldObject -> Mono.zip(Mono.just(pair.getT1()), Mono.just(oldObject), Mono.just(pair.getT2())));
        })
        .flatMap(objectsDao::updateObject);
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
}
