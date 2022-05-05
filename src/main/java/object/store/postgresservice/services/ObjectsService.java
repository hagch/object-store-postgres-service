package object.store.postgresservice.services;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.daos.ObjectsDao;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record ObjectsService(ObjectsDao objectsDao, TypeService typeService) {

  public Mono<Map<String, Object>> insertObjectByTypeName(Mono<Map<String, Object>> monoObject, String typeName) {
    return typeService.getByName(typeName)
        .flatMap(type -> objectsDao.insertObject(Mono.zip(monoObject, Mono.just(type))));
  }

  public Mono<Map<String, Object>> insertObjectByTypId(Mono<Map<String, Object>> monoObject, String typeId) {
    return typeService.getById(UUID.fromString(typeId))
        .flatMap(type -> objectsDao.insertObject(Mono.zip(monoObject, Mono.just(type))));
  }

  public Mono<Map<String, Object>> getObjectByTypId(String objectId, String typeId) {
    return typeService.getById(UUID.fromString(typeId))
        .flatMap(type -> objectsDao.getObject(Mono.zip(Mono.just(objectId), Mono.just(type))));
  }

  public Mono<Map<String, Object>> getObjectByTypName(String objectId, String typeName) {
    return typeService.getByName(typeName).flatMap(type -> objectsDao.getObject(Mono.zip(Mono.just(objectId),
        Mono.just(type))));
  }

  public Mono<Flux<Map<String, Object>>> getObjectsByTypId(String typeId) {
    return Mono.just(objectsDao.getObjects(typeService.getById(UUID.fromString(typeId))));
  }

  public Mono<Flux<Map<String, Object>>> getObjectsByTypeName(String typeName) {
    return Mono.just(objectsDao.getObjects(typeService.getByName(typeName)));
  }

  public Mono<Map<String, Object>> updateObjectsByTypeId(Mono<Map<String, Object>> monoObject, String typeId) {
    return typeService.getById(UUID.fromString(typeId)).flatMap(type -> Mono.zip(monoObject, Mono.just(type)))
        .flatMap(pair -> {
          String primaryKey = getPrimaryKeyName(pair.getT2());
          String objectId = Objects.toString(pair.getT1().get(primaryKey));
          return objectsDao.updateObject(objectsDao.getObject(Mono.zip(Mono.just(objectId), Mono.just(pair.getT2())))
              .flatMap(oldObject -> Mono.zip(Mono.just(pair.getT1()), Mono.just(oldObject), Mono.just(pair.getT2()))));
        });
  }

  public Mono<Map<String, Object>> updateObjectsByTypeName(Mono<Map<String, Object>> monoObject, String typeName) {
    return typeService.getByName(typeName).flatMap(type -> Mono.zip(monoObject, Mono.just(type)))
        .flatMap(pair -> {
          String primaryKey = getPrimaryKeyName(pair.getT2());
          String objectId = Objects.toString(pair.getT1().get(primaryKey));
          return objectsDao.updateObject(objectsDao.getObject(Mono.zip(Mono.just(objectId), Mono.just(pair.getT2())))
              .flatMap(oldObject -> Mono.zip(Mono.just(pair.getT1()), Mono.just(oldObject), Mono.just(pair.getT2()))));
        });
  }

  public Mono<Integer> deleteObjectByTypeId(String objectId, String typeId) {
    return typeService.getById(UUID.fromString(typeId)).flatMap(type -> objectsDao.deleteObject(objectId, type));
  }

  public Mono<Integer> deleteObjectByTypeName(String objectId, String typeName) {
    return typeService.getByName(typeName).flatMap(type -> objectsDao.deleteObject(objectId, type));
  }

  private String getPrimaryKeyName(TypeDto type) {
    Optional<BasicBackendDefinitionDto> primary =
        type.getBackendKeyDefinitions().stream()
            .filter(definition -> BackendKeyType.PRIMARYKEY.equals(definition.getType()))
            .findFirst();
    return primary
        .map(BasicBackendDefinitionDto::getKey)
        .orElse(Strings.EMPTY);
  }
}
