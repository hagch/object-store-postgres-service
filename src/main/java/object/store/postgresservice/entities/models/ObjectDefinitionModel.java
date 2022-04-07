package object.store.postgresservice.entities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;

/**
 * ObjectDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-07T16:01:04.243227802+02:00[Europe/Vienna]")
public class ObjectDefinitionModel extends BasicBackendDefinitionModel {

  @JsonProperty("properties")
  @Valid
  private List<BasicBackendDefinitionModel> properties = null;

  @JsonProperty("additionalProperties")
  private Boolean additionalProperties;

  public ObjectDefinitionModel properties(List<BasicBackendDefinitionModel> properties) {
    this.properties = properties;
    return this;
  }

  public ObjectDefinitionModel addPropertiesItem(BasicBackendDefinitionModel propertiesItem) {
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

  public ObjectDefinitionModel additionalProperties(Boolean additionalProperties) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ObjectDefinitionModel objectDefinition = (ObjectDefinitionModel) o;
    return Objects.equals(this.properties, objectDefinition.properties) &&
        Objects.equals(this.additionalProperties, objectDefinition.additionalProperties) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(properties, additionalProperties, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ObjectDefinition {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
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

