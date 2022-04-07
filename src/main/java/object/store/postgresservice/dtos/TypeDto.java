package object.store.postgresservice.dtos;

import java.util.List;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;

public class TypeDto {

  private String id;

  private String name;

  private boolean additionalProperties;

  private List<BasicBackendDefinitionDto> backendKeyDefinitions;

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

  public boolean getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(boolean additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  public List<BasicBackendDefinitionDto> getBackendKeyDefinitions() {
    return backendKeyDefinitions;
  }

  public void setBackendKeyDefinitions(
      List<BasicBackendDefinitionDto> backendKeyDefinitions) {
    this.backendKeyDefinitions = backendKeyDefinitions;
  }
}
