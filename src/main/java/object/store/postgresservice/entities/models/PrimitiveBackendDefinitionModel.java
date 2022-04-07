package object.store.postgresservice.entities.models;

import java.util.Objects;
import javax.annotation.Generated;

/**
 * PrimitiveDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-07T16:01:04.243227802+02:00[Europe/Vienna]")
public class PrimitiveBackendDefinitionModel extends BasicBackendDefinitionModel {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrimitiveDefinition {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
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


