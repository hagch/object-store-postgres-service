package object.store.postgresservice.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;
import object.store.postgresservice.entities.BackendKeyDefinitionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class BackendKeyDefinitionReader implements Converter<Json, Set<BackendKeyDefinitionEntity>> {

  @Override
  public Set<BackendKeyDefinitionEntity> convert(Json json) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(json.asString(), new TypeReference<>() {
      });
    } catch (Exception e) {
      Logger.getGlobal().warning("Problem while parsing JSON: {}");
    }
    return Collections.emptySet();
  }
}
