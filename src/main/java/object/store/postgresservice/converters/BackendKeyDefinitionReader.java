package object.store.postgresservice.converters;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import object.store.postgresservice.entities.BackendKeyDefintionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BackendKeyDefinitionReader implements Converter<Json, Set<BackendKeyDefintionEntity>> {

  @Override
  public Set<BackendKeyDefintionEntity> convert(Json json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      // objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
      return objectMapper.readValue(json.asString(), new TypeReference<>() {
      });
    } catch (Exception e) {
      Logger.getGlobal().warning("Problem while parsing JSON: {}");
    }
    return Collections.emptySet();
  }
}
