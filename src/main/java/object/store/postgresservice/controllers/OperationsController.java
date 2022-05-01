package object.store.postgresservice.controllers;

import object.store.gen.dbservice.apis.OperationsApi;
import object.store.gen.dbservice.models.OperationDefinition;
import object.store.postgresservice.services.OperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public record OperationsController(OperationsService operationsService) implements OperationsApi {

  @Override
  public Mono<ResponseEntity<Void>> doOperations(Flux<OperationDefinition> operationDefinition,
      ServerWebExchange exchange) {
    return operationsService.handleOperations(operationDefinition).then().map(ResponseEntity::ok);
  }
}
