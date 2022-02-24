package object.store.postgresservice;

import io.r2dbc.spi.ConnectionFactory;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.util.ResourceUtils;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class PostgresServiceApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(PostgresServiceApplication.class)
				.web(WebApplicationType.REACTIVE)
				.run(args);
	}

	@Bean
	ConnectionFactoryInitializer initializer(@Qualifier("connectionFactory") ConnectionFactory connectionFactory)
			throws FileNotFoundException {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);

		ResourceDatabasePopulator resource =
				new ResourceDatabasePopulator( new UrlResource(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX +
						"schema.sql")));
		initializer.setDatabasePopulator(resource);
		return initializer;
	}
}
