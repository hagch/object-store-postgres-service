package object.store.postgresservice.entities;

import java.io.Serializable;
import java.util.Set;

public class BackendKeyDefintionEntity implements Serializable {

  private String key;
  private String type;
  private String primitiveArrayType;
  private Set<BackendKeyDefintionEntity> properties;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPrimitiveArrayType() {
    return primitiveArrayType;
  }

  public void setPrimitiveArrayType(String primitiveArrayType) {
    this.primitiveArrayType = primitiveArrayType;
  }

  public Set<BackendKeyDefintionEntity> getProperties() {
    return properties;
  }

  public void setProperties(Set<BackendKeyDefintionEntity> properties) {
    this.properties = properties;
  }
}
