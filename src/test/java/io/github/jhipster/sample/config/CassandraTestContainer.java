package io.github.jhipster.sample.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.cassandra.core.cql.session.init.ResourceKeyspacePopulator;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

/**
 * Base class for starting/stopping Cassandra during tests.
 */
public class CassandraTestContainer implements InitializingBean, DisposableBean {

    public static final String DEFAULT_KEYSPACE_NAME = "cassandratestkeyspace";

    private static final Logger LOG = LoggerFactory.getLogger(CassandraTestContainer.class);
    private static final Integer DATABASE_REQUEST_TIMEOUT = 20;
    private static final Integer CONTAINER_STARTUP_TIMEOUT_MINUTES = 10;

    private CassandraContainer<?> cassandraContainer;

    @Override
    public void destroy() {
        if (null != cassandraContainer && cassandraContainer.isRunning()) {
            cassandraContainer.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        if (null == cassandraContainer) {
            cassandraContainer = new CassandraContainer<>("cassandra:5.0")
                .withStartupTimeout(Duration.of(CONTAINER_STARTUP_TIMEOUT_MINUTES, ChronoUnit.MINUTES))
                .withLogConsumer(new Slf4jLogConsumer(LOG))
                .withReuse(true);
        }
        if (!cassandraContainer.isRunning()) {
            cassandraContainer.start();

            CqlSession cqlSession = new CqlSessionBuilder()
                .addContactPoint(cassandraContainer.getContactPoint())
                .withLocalDatacenter(cassandraContainer.getLocalDatacenter())
                .build();
            cqlSession.execute(
                "CREATE KEYSPACE " + DEFAULT_KEYSPACE_NAME + " WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1}"
            );
            cqlSession.close();

            cqlSession = new CqlSessionBuilder()
                .addContactPoint(cassandraContainer.getContactPoint())
                .withLocalDatacenter(cassandraContainer.getLocalDatacenter())
                .withKeyspace(DEFAULT_KEYSPACE_NAME)
                .withConfigLoader(getConfigLoader())
                .build();

            new ResourceKeyspacePopulator(new PathMatchingResourcePatternResolver().getResources("config/cql/changelog/*.cql")).populate(
                cqlSession
            );

            cqlSession.close();
        }
    }

    public CassandraContainer<?> getCassandraContainer() {
        return cassandraContainer;
    }

    private DriverConfigLoader getConfigLoader() {
        return DriverConfigLoader.programmaticBuilder()
            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(DATABASE_REQUEST_TIMEOUT))
            .build();
    }
}
