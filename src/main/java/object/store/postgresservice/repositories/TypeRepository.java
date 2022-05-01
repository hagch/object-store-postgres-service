package object.store.postgresservice.repositories;

import java.util.UUID;
import object.store.postgresservice.entities.TypeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TypeRepository extends ReactiveCrudRepository<TypeEntity, UUID> {

  Mono<TypeEntity> findByName(String name);
  Mono<TypeEntity> getById(UUID id);

}
