package object.store.postgresservice.entities.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * RelationDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-07T16:01:04.243227802+02:00[Europe/Vienna]")
public class RelationDefinitionModel extends BasicBackendDefinitionModel  {

  @JsonProperty("referencedTypeId")
  private String referencedTypeId;

  @JsonProperty("referenceKey")
  private String referenceKey;

  public RelationDefinitionModel referencedTypeId(String referencedTypeId) {
    this.referencedTypeId = referencedTypeId;
    return this;
  }

  /**
   * Get referencedTypeId
   * @return referencedTypeId
   */

  @Schema(name = "referencedTypeId", required = false)
  public String getReferencedTypeId() {
    return referencedTypeId;
  }

  public void setReferencedTypeId(String referencedTypeId) {
    this.referencedTypeId = referencedTypeId;
  }

  public RelationDefinitionModel referenceKey(String referenceKey) {
    this.referenceKey = referenceKey;
    return this;
  }

  /**
   * Get referenceKey
   * @return referenceKey
   */

  @Schema(name = "referenceKey", required = false)
  public String getReferenceKey() {
    return referenceKey;
  }

  public void setReferenceKey(String referenceKey) {
    this.referenceKey = referenceKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelationDefinitionModel relationDefinition = (RelationDefinitionModel) o;
    return Objects.equals(this.referencedTypeId, relationDefinition.referencedTypeId) &&
        Objects.equals(this.referenceKey, relationDefinition.referenceKey) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(referencedTypeId, referenceKey, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelationDefinition {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    referencedTypeId: ").append(toIndentedString(referencedTypeId)).append("\n");
    sb.append("    referenceKey: ").append(toIndentedString(referenceKey)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

