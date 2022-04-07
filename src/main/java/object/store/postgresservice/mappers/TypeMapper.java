package object.store.postgresservice.mappers;

import java.util.Objects;
import java.util.UUID;
import object.store.gen.dbservice.models.Type;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.entities.TypeEntity;
import object.store.postgresservice.mappers.api.KeyDefinitionApiMapper;
import object.store.postgresservice.mappers.dto.KeyDefinitionDtoMapper;
import object.store.postgresservice.mappers.entity.KeyDefinitionEntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring", uses = {KeyDefinitionEntityMapper.class, KeyDefinitionDtoMapper.class,
    KeyDefinitionApiMapper.class})
public interface TypeMapper {

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

  @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
  TypeDto entityToDto(TypeEntity document);

  @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
  TypeEntity dtoToEntity(TypeDto type);

  Type dtoToApi(TypeDto type);

  TypeDto apiToDto(Type type);
}
