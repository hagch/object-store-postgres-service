package object.store.postgresservice.dtos;

import java.util.List;

public class TypeDto {

  private String id;

  private String name;

  private boolean additionalProperties;

  private List<BackendKeyDefinitionDto> backendKeyDefinitionDtos;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(boolean additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  public List<BackendKeyDefinitionDto> getBackendKeyDefinitionDtos() {
    return backendKeyDefinitionDtos;
  }

  public void setBackendKeyDefinitionDtos(
      List<BackendKeyDefinitionDto> backendKeyDefinitionDtos) {
    this.backendKeyDefinitionDtos = backendKeyDefinitionDtos;
  }
}
