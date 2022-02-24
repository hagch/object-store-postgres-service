package object.store.postgresservice.entities;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.w3c.dom.stylesheets.LinkStyle;

@Table("TYPE")
public class TypeEntity implements Persistable<UUID> {
  @Id
  private UUID id;

  private String name;

  @Column("ADDITIONAL_PROPERTIES")
  private boolean additionalProperties;

  @Column("BACKEND_KEY_DEFINITIONS")
  private Set<BackendKeyDefintionEntity> backendKeyDefinitions;

  public TypeEntity() {}

  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    if(Objects.isNull(this.id)){
      this.id = UUID.randomUUID();
      return true;
    }
    return false;
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

  public Set<BackendKeyDefintionEntity> getBackendKeyDefinitions() {
    return backendKeyDefinitions;
  }

  public void setBackendKeyDefinitions(
      Set<BackendKeyDefintionEntity> backendKeyDefinitions) {
    this.backendKeyDefinitions = backendKeyDefinitions;
  }
}
