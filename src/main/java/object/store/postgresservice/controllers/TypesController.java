package object.store.postgresservice.controllers;

import java.util.UUID;
import object.store.gen.dbservice.apis.TypesApi;
import object.store.gen.dbservice.models.Type;
import object.store.gen.dbservice.models.UpdateType;
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
    return type.map(mapper::apiToDto).flatMap(typeService::createType).map(mapper::dtoToApi).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteType(String id, ServerWebExchange exchange) {
    return typeService.delete(id).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Type>> getTypeById(String id, ServerWebExchange exchange) {
    return typeService.getById(UUID.fromString(id)).map(mapper::dtoToApi).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Flux<Type>>> getTypes(ServerWebExchange exchange) {
    return Mono.just(typeService.getAll().map(mapper::dtoToApi)).map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Type>> updateType(String id, Mono<UpdateType> updateType,
      ServerWebExchange exchange) {
    return updateType.flatMap( typeUpdate -> typeService.updateType(id,mapper.apiToDto(typeUpdate.getType()),
        typeUpdate.getObjects())).map(mapper::dtoToApi).map(ResponseEntity::ok);
  }
}
