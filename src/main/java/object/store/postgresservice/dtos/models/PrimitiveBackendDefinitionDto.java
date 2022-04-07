package object.store.postgresservice.dtos.models;

import object.store.gen.dbservice.models.BackendKeyType;

/**
 * BasicBackendDefinition
 */

public final class PrimitiveBackendDefinitionDto extends BasicBackendDefinitionDto {

  public PrimitiveBackendDefinitionDto(String key, Boolean isNullAble, BackendKeyType type, Boolean isUnique) {
    super(key, isNullAble, type, isUnique);
  }
}

