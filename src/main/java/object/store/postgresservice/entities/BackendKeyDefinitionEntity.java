package object.store.postgresservice.entities;

import java.io.Serializable;
import java.util.Set;

public class BackendKeyDefinitionEntity implements Serializable {

  private String key;
  private String type;
  private String primitiveArrayType;
  private boolean isNullAble;
  private Set<BackendKeyDefinitionEntity> properties;

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

  public Set<BackendKeyDefinitionEntity> getProperties() {
    return properties;
  }

  public void setProperties(Set<BackendKeyDefinitionEntity> properties) {
    this.properties = properties;
  }

  public boolean isNullAble() {
    return isNullAble;
  }

  public void setNullAble(Boolean nullAble) {
    isNullAble = Boolean.TRUE.equals(nullAble);
  }
}
