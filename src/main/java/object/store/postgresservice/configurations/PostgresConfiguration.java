package object.store.postgresservice.configurations;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;
import object.store.postgresservice.converters.BackendKeyDefinitionReader;
import object.store.postgresservice.converters.BackendKeyDefintionWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfiguration extends AbstractR2dbcConfiguration {

  private final DatasourceProperties datasourceProps;

  public PostgresConfiguration(DatasourceProperties datasourceProps){
    this.datasourceProps = datasourceProps;
  }
  @Override
  public ConnectionFactory connectionFactory() {
    return ConnectionFactories.get(datasourceProps.getPrefix() + datasourceProps.getUsername() + ":" +
        datasourceProps.getPassword() + "@" + datasourceProps.getHost() + ":" + datasourceProps.getPort() +
        "/" + datasourceProps.getDatabase());
  }


  @Bean
  @Override
  public R2dbcCustomConversions r2dbcCustomConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();
    converters.add(new BackendKeyDefintionWriter());
    converters.add(new BackendKeyDefinitionReader());
    return new R2dbcCustomConversions(getStoreConversions(), converters);
  }
}
