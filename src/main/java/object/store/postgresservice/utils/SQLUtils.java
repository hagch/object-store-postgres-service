package object.store.postgresservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public record SQLUtils(ObjectMapper mapper) {

  public Map<String, Object> mapJsonObjects(Map<String, Object> object) {
    HashMap<String, Object> newObject = new HashMap<>();
    for (Entry<String, Object> entry : object.entrySet()) {
      if (entry.getValue() instanceof Json) {
        try {
          newObject.put(entry.getKey(), mapper.readValue(((Json) entry.getValue()).asString(),
              new TypeReference<>() {
              }));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
      } else {
        newObject.put(entry.getKey(), entry.getValue());
      }
    }
    return newObject;
  }
}
