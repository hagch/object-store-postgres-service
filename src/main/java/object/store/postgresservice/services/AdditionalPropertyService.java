package object.store.postgresservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.dtos.models.BasicBackendDefinitionDto;
import org.springframework.stereotype.Service;

@Service
public record AdditionalPropertyService(ObjectMapper mapper) {

  public static String ADDITIONAL_PROPERTIES_KEY = "additionalProperties";

  public Map<String, Object> mapAdditionalProperties(Map<String, Object> object) {
    if (object.containsKey(ADDITIONAL_PROPERTIES_KEY)) {
      object.remove(ADDITIONAL_PROPERTIES_KEY);
      if (object.get(ADDITIONAL_PROPERTIES_KEY) instanceof Map<?, ?>) {
        Map<? extends String, ?> map = mapper.convertValue(object.get(ADDITIONAL_PROPERTIES_KEY), Map.class);
        object.putAll(map);
      }
    }
    return object;
  }

  public void mapToAdditionalProperties(Map<String, Object> object, TypeDto type) {
    Map<String, Object> additionalProperties = getAdditionalProperties(object, type);
    if (!additionalProperties.isEmpty()) {
      object.keySet().removeAll(additionalProperties.keySet());
      object.put(ADDITIONAL_PROPERTIES_KEY, additionalProperties);
    }
  }

  public Map<String, Object> getAdditionalProperties(Map<String, Object> object, TypeDto type) {
    Set<String> definedKeys = type.getBackendKeyDefinitions().stream().map(BasicBackendDefinitionDto::getKey)
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
