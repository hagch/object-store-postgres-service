package object.store.postgresservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import object.store.gen.dbservice.models.BackendKeyDefinition;
import object.store.gen.dbservice.models.Type;
import org.springframework.stereotype.Service;

@Service
public record AdditionalPropertyService(ObjectMapper mapper) {

  public static String ADDITIONAL_PROPERTIES_KEY = "additionalProperties";

  public Map<String, Object> mapAdditionalProperties(Map<String, Object> object) {
    if (object.containsKey(ADDITIONAL_PROPERTIES_KEY)) {
      if (object.get(ADDITIONAL_PROPERTIES_KEY) instanceof Map<?, ?>) {
        Map<? extends String, ?> map = mapper.convertValue(object.get(ADDITIONAL_PROPERTIES_KEY), Map.class);
        object.remove(ADDITIONAL_PROPERTIES_KEY);
        object.putAll(map);
      }
    }
    return object;
  }

  public void mapToAdditionalProperties(Map<String, Object> object, Type type) {
    Map<String, Object> additionalProperties = getAdditionalProperties(object, type);
    object.keySet().removeAll(additionalProperties.keySet());
    object.put(ADDITIONAL_PROPERTIES_KEY, additionalProperties);
  }

  public Map<String, Object> getAdditionalProperties(Map<String, Object> object, Type type) {
    Set<String> definedKeys = type.getBackendKeyDefinitions().stream().map(BackendKeyDefinition::getKey)
        .collect(Collectors.toSet());
    Map<String, Object> notDefinedKeys = new HashMap<>(object);
    notDefinedKeys.keySet().removeAll(definedKeys);
    return notDefinedKeys;
  }

  public Map<String, Object> mergeDifferencesWithAdditionalPropertyValues(Map<String, Object> differences,
      Map<String, Object> additionalProperties) {
    return Stream.concat(differences.entrySet().stream(), additionalProperties.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (difference, old) -> difference));
  }

}
