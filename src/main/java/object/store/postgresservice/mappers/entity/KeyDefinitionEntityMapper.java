package object.store.postgresservice.mappers.entity;

import java.util.List;
import java.util.stream.Collectors;
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
public interface KeyDefinitionEntityMapper {

  default BasicBackendDefinitionDto mapEntityToDto(BasicBackendDefinitionModel basicBackendDefinition) {
    return mapEntitiesToDtos(List.of(basicBackendDefinition)).get(0);
  }

  private List<BasicBackendDefinitionDto> mapEntitiesToDtos(List<BasicBackendDefinitionModel> definitions) {
    return definitions.stream().map(definition -> switch (definition) {
      case ArrayDefinitionModel casted -> new ArrayDefinitionDto(casted.getKey(), casted.getIsNullAble(),
          casted.getPrimitiveArrayType(), mapEntitiesToDtos(casted.getProperties()), casted.getAdditionalProperties(),
          casted.getAdditionalItems(), casted.getIsUnique());
      case ObjectDefinitionModel casted -> new ObjectDefinitionDto(casted.getKey(), casted.getIsNullAble(),
          mapEntitiesToDtos(casted.getProperties()), casted.getAdditionalProperties(), casted.getIsUnique());
      case PrimitiveBackendDefinitionModel casted -> new PrimitiveBackendDefinitionDto(casted.getKey(),
          casted.getIsNullAble(), casted.getType(), casted.getIsUnique());
      case RelationDefinitionModel casted -> new RelationDefinitionDto(casted.getKey(), casted.getIsNullAble(),
          casted.getType(), casted.getReferencedTypeId(), casted.getReferenceKey(), casted.getIsUnique());
      default -> throw new IllegalStateException("Unexpected value: " + definition);
    }).collect(Collectors.toList());
  }
}
