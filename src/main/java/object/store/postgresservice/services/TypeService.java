package object.store.postgresservice.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.postgresservice.daos.TypeDao;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.ArrayDefinitionDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.dtos.models.ObjectDefinitionDto;
import object.store.postgresservice.dtos.models.RelationDefinitionDto;
import object.store.postgresservice.exceptions.GetAllTypesFailed;
import object.store.postgresservice.exceptions.ReferencedKeyDontExistsFromType;
import object.store.postgresservice.exceptions.TypeNotFoundById;
import object.store.postgresservice.exceptions.TypeNotFoundByName;
import object.store.postgresservice.exceptions.WrongTypeId;
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

  public Mono<TypeDto> updateType(String typeId, TypeDto document, List<Map<String,Object>> objects) {
    if(!Objects.equals(typeId, document.getId())){
      return Mono.error(new WrongTypeId());
    }
    return typeDao.updateType(document,objects);
  }

  public Mono<Void> delete(String id) {
    return typeDao.delete(id);
  }

  private Mono<TypeDto> validateTypeReferences(TypeDto type) {
    return checkFoundRelation(Mono.just(type).map(TypeDto::getBackendKeyDefinitions).flatMapMany(Flux::fromIterable)).collectList().thenReturn(type);
  }

  private Flux<Void> checkFoundRelation(Flux<BasicBackendDefinitionDto> fluxDefinition){
    return fluxDefinition.filter( def -> typesToCheck.contains(def.getType())).flatMap( definitionToCheck -> switch(definitionToCheck){
      case RelationDefinitionDto definition -> validateRelation(definition);
      case ObjectDefinitionDto definition && !definition.getProperties().isEmpty()->  checkFoundRelation(Flux.fromIterable(definition.getProperties()).thenMany(Flux.empty()));
      case ArrayDefinitionDto definition && !definition.getProperties().isEmpty() -> checkFoundRelation(Flux.fromIterable(definition.getProperties())).thenMany(Flux.empty());
      case default -> Flux.empty();
    });
  }
  private Flux<Void> validateRelation(RelationDefinitionDto relation){
    return Flux.concat(getById(UUID.fromString(relation.getReferencedTypeId()))
        .flatMap(referencedType -> {
          Optional<BasicBackendDefinitionDto> optionalKey = findDefinition(relation.getReferenceKey(),
              referencedType.getBackendKeyDefinitions());
          if (optionalKey.isEmpty()) {
            return Mono.error(new ReferencedKeyDontExistsFromType(relation.getReferenceKey(), referencedType.getName()));
          }
          return typeDao.doesTableExist(referencedType.getName()).flatMap(doesExist -> {
            if (Boolean.FALSE.equals(doesExist)) {
              return Mono.error(new TypeNotFoundByName(referencedType.getName()));
            }
            return Mono.empty();
          });
        }).thenMany(Flux.empty()));
  }

  private Optional<BasicBackendDefinitionDto> findDefinition(String key,
      List<BasicBackendDefinitionDto> definitions){
    Optional<BasicBackendDefinitionDto> found = definitions.stream().filter( definition -> Objects.equals(key,
        definition.getKey())).findFirst();
    if(found.isEmpty()){
      Optional<Optional<BasicBackendDefinitionDto>> optional =
          definitions.stream().filter( definitionDto -> definitionDto instanceof ArrayDefinitionDto || definitionDto instanceof ObjectDefinitionDto)
              .map( definition -> {
                if(definition instanceof  ArrayDefinitionDto casted && Objects.nonNull(casted.getProperties()) && !casted.getProperties().isEmpty()){
                  return casted.getProperties();
                }
                if(definition instanceof ObjectDefinitionDto casted && Objects.nonNull(casted.getProperties())){
                  return casted.getProperties();
                }
                return Collections.emptyList();
              }).map( foundDefinitions -> findDefinition(key, (List<BasicBackendDefinitionDto>) foundDefinitions)).filter(
                  Optional::isPresent).findFirst();
      found = optional.orElse(found);
    }
    return found;
  }
}
