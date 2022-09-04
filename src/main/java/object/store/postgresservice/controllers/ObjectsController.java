package object.store.postgresservice.controllers;

import java.util.Map;
import object.store.gen.objects.apis.ObjectsApi;
import object.store.postgresservice.services.ObjectsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public record ObjectsController(ObjectsService objectsService) implements ObjectsApi {

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> createObjectById(String id, Mono<Map<String, Object>> requestBody,
      ServerWebExchange exchange) {
    return objectsService.insertObjectByTypId(requestBody, id).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteObjectById(String id, String objectId, ServerWebExchange exchange) {
    return objectsService.deleteObjectByTypeId(objectId, id).then().map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> getObjectById(String id, String objectId,
      ServerWebExchange exchange) {
    return objectsService.getObjectByTypId(objectId, id).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<Map<String, Object>>>> getObjectsById(String id, ServerWebExchange exchange) {
    return objectsService.getObjectsByTypId(id).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> updateObjectById(String id, String objectId,
      Mono<Map<String, Object>> requestBody, ServerWebExchange exchange) {
    return objectsService.updateObjectsByTypeId(requestBody, id).map(ResponseEntity::ok);
  }
}
