package object.store.postgresservice.controllers;

import java.util.Map;
import object.store.gen.objects.apis.ObjectsApi;
import object.store.gen.objects.models.Identifier;
import object.store.postgresservice.services.ObjectsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public record ObjectsController(ObjectsService objectsService) implements ObjectsApi {

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> createObjectByTypeIdentifier(Identifier identifierType,
      String identifier, Mono<Map<String, Object>> requestBody, ServerWebExchange exchange) {
    return switch (identifierType) {
      case IDS -> objectsService.insertObjectByTypId(requestBody, identifier).map(ResponseEntity::ok);
      case NAMES -> objectsService.insertObjectByTypeName(requestBody, identifier).map(ResponseEntity::ok);
    };
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteObjectByTypeIdentifier(Identifier identifierType,
      String identifier, String objectId, ServerWebExchange exchange) {
    return switch (identifierType) {
      case IDS -> objectsService.deleteObjectByTypeId(objectId, identifier).then().map(ResponseEntity::ok);
      case NAMES -> objectsService.deleteObjectByTypeName(objectId, identifier).then().map(ResponseEntity::ok);
    };
  }

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> getObjectByTypeIdentifier(Identifier identifierType,
      String identifier, String objectId, ServerWebExchange exchange) {
    return switch (identifierType) {
      case IDS -> objectsService.getObjectByTypId(objectId, identifier).map(ResponseEntity::ok);
      case NAMES -> objectsService.getObjectByTypName(objectId, identifier).map(ResponseEntity::ok);
    };
  }

  @Override
  public Mono<ResponseEntity<Flux<Map<String, Object>>>> getObjectsByTypeIdentifier(Identifier identifierType,
      String identifier, ServerWebExchange exchange) {
    return switch (identifierType) {
      case IDS -> objectsService.getObjectsByTypId(identifier).map(ResponseEntity::ok);
      case NAMES -> objectsService.getObjectsByTypeName(identifier).map(ResponseEntity::ok);
    };
  }

  @Override
  public Mono<ResponseEntity<Map<String, Object>>> updateObjectByTypeIdentifier(Identifier identifierType,
      String identifier, String objectId, Mono<Map<String, Object>> requestBody, ServerWebExchange exchange) {
    return switch (identifierType) {
      case IDS -> objectsService.updateObjectsByTypeId(requestBody, identifier).map(ResponseEntity::ok);
      case NAMES -> objectsService.updateObjectsByTypeName(requestBody, identifier).map(ResponseEntity::ok);
    };
  }
}
