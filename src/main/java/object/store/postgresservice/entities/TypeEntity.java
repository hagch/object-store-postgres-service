package object.store.postgresservice.entities;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import object.store.postgresservice.entities.models.BasicBackendDefinitionModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("TYPE")
public class TypeEntity implements Persistable<UUID> {

  @Id
  private UUID id;

  private String name;

  @Column("ADDITIONAL_PROPERTIES")
  private boolean additionalProperties;

  @Column("BACKEND_KEY_DEFINITIONS")
  private Set<BasicBackendDefinitionModel> backendKeyDefinitions;

  public TypeEntity() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public Set<BasicBackendDefinitionModel> getBackendKeyDefinitions() {
    return backendKeyDefinitions;
  }

  public void setBackendKeyDefinitions(Set<BasicBackendDefinitionModel> backendKeyDefinitions) {
    this.backendKeyDefinitions = backendKeyDefinitions;
  }

  @Override
  public boolean isNew() {
    if (Objects.isNull(this.id)) {
      this.id = UUID.randomUUID();
      return true;
    }
    return false;
  }
}
