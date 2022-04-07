package object.store.postgresservice.entities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import object.store.gen.dbservice.models.BackendKeyType;

/**
 * BasicBackendDefinition
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ArrayDefinitionModel.class, name = "array"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "boolean"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "date"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "double"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "integer"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "long"),
    @JsonSubTypes.Type(value = ObjectDefinitionModel.class, name = "object"),
    @JsonSubTypes.Type(value = RelationDefinitionModel.class, name = "oneToMany"),
    @JsonSubTypes.Type(value = RelationDefinitionModel.class, name = "oneToOne"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "primaryKey"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "string"),
    @JsonSubTypes.Type(value = PrimitiveBackendDefinitionModel.class, name = "timestamp"),
})
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-07T16:01:04.243227802+02:00[Europe/Vienna]")
public class BasicBackendDefinitionModel {

  @JsonProperty("key")
  private String key;

  @JsonProperty("isNullAble")
  private Boolean isNullAble;

  @JsonProperty("type")
  private BackendKeyType type;

  @JsonProperty("isUnique")
  private Boolean isUnique = false;

  public BasicBackendDefinitionModel key(String key) {
    this.key = key;
    return this;
  }

  /**
   * Get key
   *
   * @return key
   */

  @Schema(name = "key", required = false)
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public BasicBackendDefinitionModel isNullAble(Boolean isNullAble) {
    this.isNullAble = isNullAble;
    return this;
  }

  /**
   * Get isNullAble
   *
   * @return isNullAble
   */

  @Schema(name = "isNullAble", required = false)
  public Boolean getIsNullAble() {
    return isNullAble;
  }

  public void setIsNullAble(Boolean isNullAble) {
    this.isNullAble = isNullAble;
  }

  public BasicBackendDefinitionModel type(BackendKeyType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   *
   * @return type
   */
  @NotNull
  @Valid
  @Schema(name = "type", required = true)
  public BackendKeyType getType() {
    return type;
  }

  public void setType(BackendKeyType type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BasicBackendDefinitionModel basicBackendDefinition = (BasicBackendDefinitionModel) o;
    return Objects.equals(this.key, basicBackendDefinition.key) &&
        Objects.equals(this.isNullAble, basicBackendDefinition.isNullAble) &&
        Objects.equals(this.type, basicBackendDefinition.type);
  }

  public BasicBackendDefinitionModel isUnique(Boolean isUnique) {
    this.isUnique = isUnique;
    return this;
  }

  /**
   * Get isUnique
   *
   * @return isUnique
   */

  @Schema(name = "isUnique", required = false)
  public Boolean getIsUnique() {
    return isUnique;
  }

  public void setIsUnique(Boolean isUnique) {
    this.isUnique = isUnique;
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, isNullAble, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BasicBackendDefinition {\n");
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    isNullAble: ").append(toIndentedString(isNullAble)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

