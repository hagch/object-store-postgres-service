package object.store.postgresservice.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.util.Set;
import java.util.logging.Logger;
import object.store.postgresservice.entities.models.BasicBackendDefinitionModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BackendKeyDefintionWriter implements Converter<Set<BasicBackendDefinitionModel>, Json> {

  @Override
  public Json convert(Set<BasicBackendDefinitionModel> source) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return Json.of(mapper.writeValueAsString(source));
    } catch (JsonProcessingException e) {
      Logger.getGlobal().warning("Error occurred while serializing map to JSON: {}");
    }
    return Json.of("");
  }
}
