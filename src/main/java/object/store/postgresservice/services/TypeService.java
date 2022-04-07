package object.store.postgresservice.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.daos.TypeDao;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.dtos.models.RelationDefinitionDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeService {

  private static final List<BackendKeyType> typesToCheck = List.of(BackendKeyType.ONETOONE, BackendKeyType.ONETOMANY);
  private final TypeDao typeDao;

  public TypeService(TypeDao typeDao) {
    this.typeDao = typeDao;
  }

  public Mono<TypeDto> getById(UUID id) {
    return typeDao.getById(id);
  }

  public Mono<TypeDto> getByName(String name) {
    return typeDao.getByName(name);
  }

  public Flux<TypeDto> getAll() {
    return typeDao.getAll();
  }

  public Mono<TypeDto> createType(TypeDto document) {
    return validateTypeReferences(document).flatMap(typeDao::createTableForType).flatMap(typeDao::createType);
  }

  public Mono<TypeDto> updateById(TypeDto document) {
    return validateTypeReferences(document).flatMap(typeDao::updateTypeById);
  }

  public Mono<Void> delete(String id) {
    return typeDao.delete(id);
  }

  private Mono<TypeDto> validateTypeReferences(TypeDto type) {
    return Mono.just(type).map(TypeDto::getBackendKeyDefinitions).flatMapMany(Flux::fromIterable)
        .filter(def -> typesToCheck.contains(def.getType())).flatMap(definitionToCheck -> {
          RelationDefinitionDto definition = (RelationDefinitionDto) definitionToCheck;
          return getById(UUID.fromString(definition.getReferencedTypeId()))
              .switchIfEmpty(Mono.error(new IllegalStateException("referencedType does not exist")))
              .flatMap(referencedType -> {
                Optional<BasicBackendDefinitionDto> optionalKey = referencedType.getBackendKeyDefinitions().stream()
                    .filter(def -> def.getKey().equals(((RelationDefinitionDto) definitionToCheck).getReferenceKey()))
                    .findFirst();
                if (optionalKey.isEmpty()) {
                  return Mono.empty().switchIfEmpty(Mono.error(new IllegalStateException("Key does not exist")));
                }
                return typeDao.doesTableExist(referencedType.getName()).flatMap(doesExist -> {
                  if (Boolean.FALSE.equals(doesExist)) {
                    return Mono.error(new IllegalStateException("Table "
                        + "does not exist"));
                  }
                  return Mono.empty();
                });
              });
        }).collectList().thenReturn(type);
  }
}
