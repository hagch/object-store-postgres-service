package object.store.postgresservice.daos;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;
import object.store.postgresservice.dtos.TypeDto;
import object.store.postgresservice.exceptions.TypeNotFoundById;
import object.store.postgresservice.exceptions.TypeNotFoundByName;
import object.store.postgresservice.mappers.TypeMapper;
import object.store.postgresservice.repositories.TypeRepository;
import object.store.postgresservice.services.builders.sql.statement.SQLStatement;
import object.store.postgresservice.utils.UtilsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
public class TypeDao {

  private final TypeRepository typeRepository;
  private final TypeMapper typeMapper;
  private final UtilsService utilsService;

  public TypeDao(TypeRepository typeRepository, TypeMapper typeMapper, UtilsService utilsService) {
    this.typeRepository = typeRepository;
    this.typeMapper = typeMapper;
    this.utilsService = utilsService;
  }

  public Flux<TypeDto> getAll() {
    return typeRepository.findAll().map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> getById(UUID id) {
    return typeRepository.getById(id).switchIfEmpty(Mono.error(new TypeNotFoundById(id.toString())))
        .map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> getByName(String name) {
    return typeRepository.findByName(name).switchIfEmpty(Mono.error(new TypeNotFoundByName(name)))
        .map(typeMapper::entityToDto);
  }

  public Mono<TypeDto> createType(TypeDto typeDto) {
    return typeRepository.save(typeMapper.dtoToEntity(typeDto)).map(typeMapper::entityToDto);
  }

  @Transactional
  public Mono<TypeDto> updateType(TypeDto document, List<Map<String, Object>> objects) {
    return getById(UUID.fromString(document.getId())).flatMap(oldType -> dropTable(oldType.getName()))
        .thenReturn(document)
        .flatMap(this::createTableForType)
        .flatMap(this::createType)
        .flatMap(t -> Flux.fromIterable(objects).flatMap(object -> utilsService.insertObject(
            Mono.just(
                Tuples.of(object, document)))).collectList().thenReturn(t));
  }

  public Mono<TypeDto> createTableForType(TypeDto typeDto) {
    return utilsService.sqlBuilder().createTable(typeDto).map(SQLStatement::getStatement)
        .flatMap(statement -> utilsService.client().sql(statement).then()).thenReturn(typeDto);
  }

  public Mono<Void> delete(String id) {
    return typeRepository.findById(UUID.fromString(id))
        .switchIfEmpty(Mono.error(new TypeNotFoundById(id)))
        .flatMap(document -> Mono.zip(typeRepository.delete(document),
            dropTable(document.getName()).then()).then());
  }

  private Mono<Void> dropTable(String name) {
    return utilsService.client().sql("DROP TABLE " + Strings.dquote(name) + " ;").then();
  }

  public Mono<Boolean> doesTableExist(String tableName) {
    String call = "'public." + tableName + "'";
    return utilsService.client().sql("SELECT to_regclass( " + call + ");").fetch().first().map(result -> Boolean.TRUE)
        .defaultIfEmpty(Boolean.FALSE).log();
  }
}
