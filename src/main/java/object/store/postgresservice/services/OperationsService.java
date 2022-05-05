package object.store.postgresservice.services;

import static object.store.gen.dbservice.models.OperationDefinition.OperationTypeEnum.UPDATE;
import static object.store.gen.dbservice.models.OperationDefinition.OperationTypeEnum.CREATE;

import java.util.List;
import object.store.gen.dbservice.models.CreateUpdateOperationDefinition;
import object.store.gen.dbservice.models.DeleteOperationDefinition;
import object.store.gen.dbservice.models.OperationDefinition;
import object.store.postgresservice.exceptions.NoOperationExecuted;
import object.store.postgresservice.exceptions.OperationNotSupported;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record OperationsService(ObjectsService objectsService,
                                TransactionalOperator transactionalOperator, ReactiveTransactionManager transactionManager) {
  public Mono<List<Object>> handleOperations(Flux<OperationDefinition> fluxOperations) {
    return fluxOperations.flatMap(operation -> switch (operation) {
      case CreateUpdateOperationDefinition casted && CREATE.equals(casted.getOperationType()) -> objectsService.insertObjectByTypId(Mono.just(casted.getObject()),
          operation.getTypeReferenceId()).log();
      case CreateUpdateOperationDefinition casted && UPDATE.equals(casted.getOperationType()) ->
          objectsService.updateObjectsByTypeId(Mono.just(casted.getObject()), operation.getTypeReferenceId());
      case DeleteOperationDefinition casted -> objectsService.deleteObjectByTypeId(casted.getObjectId(),
          operation.getTypeReferenceId());
      default -> Mono.error(new OperationNotSupported(operation.toString()));
    }).collectList().flatMap( results -> {
      if(results.isEmpty()){
        return Mono.error(new NoOperationExecuted());
      }
      return Mono.just(results);
    }).as(transactionalOperator::transactional);
  }
}
