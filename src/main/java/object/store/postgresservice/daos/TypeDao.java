package object.store.postgresservice.daos;

import java.util.UUID;
import object.store.postgresservice.builders.SQLStatementBuilder;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.mappers.TypeMapper;
import object.store.postgresservice.repositories.TypeRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public record TypeDao(TypeRepository typeRepository, TypeMapper typeMapper, DatabaseClient client,
                      SQLStatementBuilder sqlBuilder, R2dbcEntityTemplate template) {

  public Flux<TypeDto> getAll() {
    return typeRepository.findAll().map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> getById(UUID id) {
    return typeRepository.findById(id).map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> getByName(String name) {
    return typeRepository.findByName(name).map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> createType(TypeDto typeDto) {
    return typeRepository.save(typeMapper.dtoToEntity(typeDto)).map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> updateTypeById(TypeDto document) {
    return typeRepository.save(typeMapper.dtoToEntity(document)).map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> createTableForType(TypeDto typeDto) {
    return client.sql(sqlBuilder.createTable(typeDto).getStatement()).then().thenReturn(typeDto);
  }

  public Mono<Void> delete(String id){
    return typeRepository.findById(UUID.fromString(id)).flatMap(document -> Mono.zip(typeRepository.delete(document),
        client.sql("DROP TABLE " + Strings.dquote(document.getName()) + " ;").then()).then());
  }
}
