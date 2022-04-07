package object.store.postgresservice.mappers.dto;

import java.util.List;
import java.util.stream.Collectors;
import object.store.gen.dbservice.models.ArrayDefinition;
import object.store.gen.dbservice.models.BasicBackendDefinition;
import object.store.gen.dbservice.models.ObjectDefinition;
import object.store.gen.dbservice.models.PrimitiveDefinition;
import object.store.gen.dbservice.models.RelationDefinition;
import object.store.postgresservice.dtos.models.ArrayDefinitionDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import object.store.postgresservice.dtos.models.ObjectDefinitionDto;
import object.store.postgresservice.dtos.models.PrimitiveBackendDefinitionDto;
import object.store.postgresservice.dtos.models.RelationDefinitionDto;
import object.store.postgresservice.entities.models.ArrayDefinitionModel;
import object.store.postgresservice.entities.models.BasicBackendDefinitionModel;
import object.store.postgresservice.entities.models.ObjectDefinitionModel;
import object.store.postgresservice.entities.models.PrimitiveBackendDefinitionModel;
import object.store.postgresservice.entities.models.RelationDefinitionModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeyDefinitionDtoMapper {

  default BasicBackendDefinition mapDtoToApi(BasicBackendDefinitionDto basicBackendDefinition) {
    return mapDefinitionDtosToApi(List.of(basicBackendDefinition)).get(0);
  }

  default BasicBackendDefinitionModel mapDtoToEntity(BasicBackendDefinitionDto basicBackendDefinition) {
    return mapDefinitionDtosToEntity(List.of(basicBackendDefinition)).get(0);
  }

  private List<BasicBackendDefinition> mapDefinitionDtosToApi(List<BasicBackendDefinitionDto> definitions) {
    return definitions.stream().map(definition -> switch (definition) {
      case ArrayDefinitionDto casted -> new ArrayDefinition().properties(mapDefinitionDtosToApi(casted.getProperties()))
          .additionalItems(casted.getAdditionalItems()).additionalProperties(casted.getAdditionalProperties())
          .primitiveArrayType(casted.getPrimitiveArrayType()).type(casted.getType()).key(casted.getKey())
          .isNullAble(casted.getIsNullAble()).isUnique(casted.getIsUnique());
      case ObjectDefinitionDto casted -> new ObjectDefinition().additionalProperties(casted.getAdditionalProperties())
          .properties(mapDefinitionDtosToApi(casted.getProperties())).type(casted.getType())
          .isNullAble(casted.getIsNullAble()).key(casted.getKey()).isUnique(casted.getIsUnique());
      case PrimitiveBackendDefinitionDto casted -> new PrimitiveDefinition().type(casted.getType())
          .isNullAble(casted.getIsNullAble()).key(casted.getKey()).isUnique(casted.getIsUnique());
      case RelationDefinitionDto casted -> new RelationDefinition().referencedTypeId(casted.getReferencedTypeId())
          .referenceKey(casted.getReferenceKey()).type(casted.getType()).isNullAble(casted.getIsNullAble())
          .key(casted.getKey()).isUnique(casted.getIsUnique());
      default -> throw new IllegalStateException("Unexpected value: " + definition);
    }).collect(Collectors.toList());
  }

  private List<BasicBackendDefinitionModel> mapDefinitionDtosToEntity(List<BasicBackendDefinitionDto> definitions) {
    return definitions.stream().map(definition -> switch (definition) {
      case ArrayDefinitionDto casted -> new ArrayDefinitionModel().properties(
              mapDefinitionDtosToEntity(casted.getProperties())).additionalItems(casted.getAdditionalItems())
          .additionalProperties(casted.getAdditionalProperties()).primitiveArrayType(casted.getPrimitiveArrayType())
          .type(casted.getType()).key(casted.getKey()).isNullAble(casted.getIsNullAble())
          .isUnique(casted.getIsUnique());
      case ObjectDefinitionDto casted -> new ObjectDefinitionModel().additionalProperties(
              casted.getAdditionalProperties()).properties(mapDefinitionDtosToEntity(casted.getProperties()))
          .type(casted.getType()).isNullAble(casted.getIsNullAble()).key(casted.getKey())
          .isUnique(casted.getIsUnique());
      case PrimitiveBackendDefinitionDto casted -> new PrimitiveBackendDefinitionModel().type(casted.getType())
          .isNullAble(casted.getIsNullAble()).key(casted.getKey()).isUnique(casted.getIsUnique());
      case RelationDefinitionDto casted -> new RelationDefinitionModel().referencedTypeId(casted.getReferencedTypeId())
          .referenceKey(casted.getReferenceKey()).type(casted.getType()).isNullAble(casted.getIsNullAble())
          .key(casted.getKey()).isUnique(casted.getIsUnique());
      default -> throw new IllegalStateException("Unexpected value: " + definition);
    }).collect(Collectors.toList());
  }
}
