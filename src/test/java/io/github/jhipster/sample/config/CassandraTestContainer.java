package io.github.jhipster.sample.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.github.dockerjava.api.command.InspectContainerResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.cassandra.core.cql.session.init.ResourceKeyspacePopulator;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@TestConfiguration(proxyBeanMethods = false)
public class CassandraTestContainer {

    public static final String DEFAULT_KEYSPACE_NAME = "cassandratestkeyspace";

    private static final Logger LOG = LoggerFactory.getLogger(CassandraTestContainer.class);
    private static final int DATABASE_REQUEST_TIMEOUT_SECONDS = 20;
    private static final int CONTAINER_STARTUP_TIMEOUT_MINUTES = 10;

    // The keyspace must exist before the application's CqlSession connects, so it is created as part of the
    // container startup instead of an eager bean the application context would have to depend on.
    private static final CassandraContainer CASSANDRA_CONTAINER = new CassandraContainer("cassandra:6.0") {
        @Override
        protected void containerIsStarted(InspectContainerResponse containerInfo) {
            super.containerIsStarted(containerInfo);
            createKeyspace(this);
        }
    }
        .withStartupTimeout(Duration.of(CONTAINER_STARTUP_TIMEOUT_MINUTES, ChronoUnit.MINUTES))
        .withLogConsumer(new Slf4jLogConsumer(LOG));

    @Bean
    @ServiceConnection
    CassandraContainer cassandraContainer() {
        return CASSANDRA_CONTAINER;
    }

    @Bean
    DynamicPropertyRegistrar cassandraProperties(CassandraContainer cassandraContainer) {
        return registry -> registry.add("spring.cassandra.keyspace-name", () -> DEFAULT_KEYSPACE_NAME);
    }

    private static void createKeyspace(CassandraContainer container) {
        try (
            CqlSession session = new CqlSessionBuilder()
                .addContactPoint(container.getContactPoint())
                .withLocalDatacenter(container.getLocalDatacenter())
                .build()
        ) {
            session.execute(
                "CREATE KEYSPACE IF NOT EXISTS " +
                    DEFAULT_KEYSPACE_NAME +
                    " WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1}"
            );
        }
        try (
            CqlSession session = new CqlSessionBuilder()
                .addContactPoint(container.getContactPoint())
                .withLocalDatacenter(container.getLocalDatacenter())
                .withKeyspace(DEFAULT_KEYSPACE_NAME)
                .withConfigLoader(
                    DriverConfigLoader.programmaticBuilder()
                        .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(DATABASE_REQUEST_TIMEOUT_SECONDS))
                        .build()
                )
                .build()
        ) {
            new ResourceKeyspacePopulator(new PathMatchingResourcePatternResolver().getResources("config/cql/changelog/*.cql")).populate(
                session
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to populate the '" + DEFAULT_KEYSPACE_NAME + "' keyspace", e);
        }
    }
}
