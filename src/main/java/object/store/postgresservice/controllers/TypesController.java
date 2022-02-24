package object.store.postgresservice.controllers;

import java.util.UUID;
import object.store.gen.dbservice.apis.TypesApi;
import object.store.gen.dbservice.models.Type;
import object.store.postgresservice.mappers.TypeMapper;
import object.store.postgresservice.services.TypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public record TypesController(TypeService typeService, TypeMapper mapper) implements TypesApi {

  @Override
  public Mono<ResponseEntity<Type>> createType(Mono<Type> type, ServerWebExchange exchange) {
    return type.flatMap(typeService::createType).flatMap(typeService::createTable).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Type>> getTypeById(String id, ServerWebExchange exchange) {
    return typeService.getById(UUID.fromString(id)).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<Type>>> getTypes(ServerWebExchange exchange) {
    return typeService.getAll().map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Type>> updateTypeById(String id, Mono<Type> type, ServerWebExchange exchange) {
    return type.doOnNext(typeService::updateById).map(ResponseEntity::ok);
  }
}
