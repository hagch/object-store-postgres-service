package object.store.postgresservice.dtos;

import java.io.Serializable;
import java.util.List;
import object.store.gen.dbservice.models.BackendKeyType;

public class BackendKeyDefinitionDto implements Serializable {

  private String key;
  private BackendKeyType type;
  private BackendKeyType primitiveArrayType;
  private List<BackendKeyDefinitionDto> properties;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public BackendKeyType getType() {
    return type;
  }

  public void setType(BackendKeyType type) {
    this.type = type;
  }

  public BackendKeyType getPrimitiveArrayType() {
    return primitiveArrayType;
  }

  public void setPrimitiveArrayType(BackendKeyType primitiveArrayType) {
    this.primitiveArrayType = primitiveArrayType;
  }

  public List<BackendKeyDefinitionDto> getProperties() {
    return properties;
  }

  public void setProperties(List<BackendKeyDefinitionDto> properties) {
    this.properties = properties;
  }
}
