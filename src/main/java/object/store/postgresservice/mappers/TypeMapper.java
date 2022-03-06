package object.store.postgresservice.mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import object.store.gen.dbservice.models.BackendKeyDefinition;
import object.store.gen.dbservice.models.BackendKeyType;
import object.store.gen.dbservice.models.Type;
import object.store.postgresservice.dtos.BackendKeyDefinitionDto;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.entities.BackendKeyDefinitionEntity;
import object.store.postgresservice.entities.TypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public interface TypeMapper {

  @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
  @Mapping(target = "backendKeyDefinitionDtos", expression = "java( "
      + "mapPropertiesEntityToDto( document.getBackendKeyDefinitions()))")
  TypeDto entityToDto(TypeEntity document);

  @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
  @Mapping(target = "backendKeyDefinitions", expression = "java( "
      + "mapPropertiesDtoToEntity(type.getBackendKeyDefinitionDtos()))")
  TypeEntity dtoToEntity(TypeDto type);

  @Mapping(target = "backendKeyDefinitions", expression = "java"
      + "(mapPropertiesToApi(type.getBackendKeyDefinitionDtos()) )")
  Type dtoToApi(TypeDto type);

  @Mapping(target = "backendKeyDefinitionDtos", expression = "java"
      + "(mapPropertiesToDto(type.getBackendKeyDefinitions()) )")
  TypeDto apiToDto(Type type);

  @Named("uuidToString")
  static String uuidToString(UUID uuid) {
    if (Objects.isNull(uuid)) {
      return null;
    }
    return uuid.toString();
  }

  @Named("stringToUuid")
  static UUID stringToUuid(String uuid) {
    if (StringUtils.hasLength(uuid)) {
      return UUID.fromString(uuid);
    }
    return null;
  }

  default List<BackendKeyDefinitionDto> mapPropertiesToDto(List<BackendKeyDefinition> definitions) {
    if (Objects.isNull(definitions) || definitions.isEmpty()) {
      return Collections.emptyList();
    }
    return definitions.stream().map(definition -> {
      BackendKeyDefinitionDto dto = new BackendKeyDefinitionDto();
      dto.setProperties(mapPropertiesToDto(definition.getProperties()));
      dto.setNullAble(definition.getIsNullAble());
      dto.setType(definition.getType());
      dto.setKey(definition.getKey());
      dto.setPrimitiveArrayType(definition.getPrimitiveArrayType());
      dto.setAdditionalProperties(definition.getAdditionalPropertis());
      return dto;
    }).collect(Collectors.toList());
  }

  default Set<BackendKeyDefinitionEntity> mapPropertiesDtoToEntity(List<BackendKeyDefinitionDto> definitions) {
    if (Objects.isNull(definitions) || definitions.isEmpty()) {
      return Collections.emptySet();
    }
    return definitions.stream().map(definition -> {
      BackendKeyDefinitionEntity entity = new BackendKeyDefinitionEntity();
      entity.setProperties(mapPropertiesDtoToEntity(definition.getProperties()));
      entity.setNullAble(definition.isNullAble());
      entity.setType(definition.getType().name());
      entity.setKey(definition.getKey());
      if (Objects.nonNull(definition.getPrimitiveArrayType())) {
        entity.setPrimitiveArrayType(definition.getPrimitiveArrayType().name());
      }
      entity.setAdditionalProperties(definition.getAdditionalProperties());
      return entity;
    }).collect(Collectors.toSet());
  }

  default List<BackendKeyDefinitionDto> mapPropertiesEntityToDto(Set<BackendKeyDefinitionEntity> definitions) {
    if (Objects.isNull(definitions) || definitions.isEmpty()) {
      return Collections.emptyList();
    }
    return definitions.stream().map(definition -> {
      BackendKeyDefinitionDto dto = new BackendKeyDefinitionDto();
      dto.setProperties(mapPropertiesEntityToDto(definition.getProperties()));
      dto.setNullAble(definition.isNullAble());
      dto.setType(BackendKeyType.valueOf(definition.getType()));
      dto.setKey(definition.getKey());
      dto.setAdditionalProperties(definition.getAdditionalProperties());
      if (StringUtils.hasLength(definition.getPrimitiveArrayType())) {
        dto.setPrimitiveArrayType(BackendKeyType.valueOf(definition.getPrimitiveArrayType()));
      }
      return dto;
    }).collect(Collectors.toList());
  }

  default List<BackendKeyDefinition> mapPropertiesToApi(List<BackendKeyDefinitionDto> definitions) {
    if (Objects.isNull(definitions) || definitions.isEmpty()) {
      return Collections.emptyList();
    }
    return definitions.stream().map(definition -> {
      BackendKeyDefinition dto = new BackendKeyDefinition();
      dto.setIsNullAble(definition.isNullAble());
      dto.setProperties(mapPropertiesToApi(definition.getProperties()));
      dto.setType(definition.getType());
      dto.setKey(definition.getKey());
      dto.setAdditionalPropertis(definition.getAdditionalProperties());
      dto.setPrimitiveArrayType(definition.getPrimitiveArrayType());
      return dto;
    }).collect(Collectors.toList());
  }
}
