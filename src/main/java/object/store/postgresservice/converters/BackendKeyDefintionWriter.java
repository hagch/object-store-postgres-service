package object.store.postgresservice.converters;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.util.Set;
import java.util.logging.Logger;
import object.store.postgresservice.entities.BackendKeyDefintionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class BackendKeyDefintionWriter implements Converter<Set<BackendKeyDefintionEntity>, Json> {

  @Override
  public Json convert(Set<BackendKeyDefintionEntity> source) {
    ObjectMapper mapper = new ObjectMapper();
    // mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    try {
      return Json.of(mapper.writeValueAsString(source));
    } catch (JsonProcessingException e) {
      Logger.getGlobal().warning("Error occurred while serializing map to JSON: {}");
    }
    return Json.of("");
  }
}
