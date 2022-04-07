package object.store.postgresservice.entities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import object.store.gen.dbservice.models.BackendKeyType;

/**
 * ArrayDefinition
 */

public class ArrayDefinitionModel extends BasicBackendDefinitionModel {

  @JsonProperty("primitiveArrayType")
  private BackendKeyType primitiveArrayType;

  @JsonProperty("properties")
  @Valid
  private List<BasicBackendDefinitionModel> properties = null;

  @JsonProperty("additionalProperties")
  private Boolean additionalProperties;

  @JsonProperty("additionalItems")
  private Boolean additionalItems;

  public ArrayDefinitionModel primitiveArrayType(BackendKeyType primitiveArrayType) {
    this.primitiveArrayType = primitiveArrayType;
    return this;
  }

  /**
   * Get primitiveArrayType
   *
   * @return primitiveArrayType
   */
  @Valid
  @Schema(name = "primitiveArrayType", required = false)
  public BackendKeyType getPrimitiveArrayType() {
    return primitiveArrayType;
  }

  public void setPrimitiveArrayType(BackendKeyType primitiveArrayType) {
    this.primitiveArrayType = primitiveArrayType;
  }

  public ArrayDefinitionModel properties(List<BasicBackendDefinitionModel> properties) {
    this.properties = properties;
    return this;
  }

  public ArrayDefinitionModel addPropertiesItem(BasicBackendDefinitionModel propertiesItem) {
    if (this.properties == null) {
      this.properties = new ArrayList<>();
    }
    this.properties.add(propertiesItem);
    return this;
  }

  /**
   * Get properties
   *
   * @return properties
   */
  @Valid
  @Schema(name = "properties", required = false)
  public List<BasicBackendDefinitionModel> getProperties() {
    return properties;
  }

  public void setProperties(List<BasicBackendDefinitionModel> properties) {
    this.properties = properties;
  }

  public ArrayDefinitionModel additionalProperties(Boolean additionalProperties) {
    this.additionalProperties = additionalProperties;
    return this;
  }

  /**
   * Get additionalProperties
   *
   * @return additionalProperties
   */

  @Schema(name = "additionalProperties", required = false)
  public Boolean getAdditionalProperties() {
    return additionalProperties;
  }

  public void setAdditionalProperties(Boolean additionalProperties) {
    this.additionalProperties = additionalProperties;
  }

  public ArrayDefinitionModel additionalItems(Boolean additionalItems) {
    this.additionalItems = additionalItems;
    return this;
  }

  /**
   * Get additionalItems
   *
   * @return additionalItems
   */

  @Schema(name = "additionalItems", required = false)
  public Boolean getAdditionalItems() {
    return additionalItems;
  }

  public void setAdditionalItems(Boolean additionalItems) {
    this.additionalItems = additionalItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArrayDefinitionModel arrayDefinition = (ArrayDefinitionModel) o;
    return Objects.equals(this.primitiveArrayType, arrayDefinition.primitiveArrayType) &&
        Objects.equals(this.properties, arrayDefinition.properties) &&
        Objects.equals(this.additionalProperties, arrayDefinition.additionalProperties) &&
        Objects.equals(this.additionalItems, arrayDefinition.additionalItems) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primitiveArrayType, properties, additionalProperties, additionalItems, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ArrayDefinition {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    primitiveArrayType: ").append(toIndentedString(primitiveArrayType)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("    additionalItems: ").append(toIndentedString(additionalItems)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

