package io.github.jhipster.sample.config;

import io.github.jhipster.sample.AbstractCassandraTest;
import io.github.jhipster.sample.config.cassandra.CassandraConfiguration;

import io.github.jhipster.config.JHipsterConstants;

import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(JHipsterConstants.SPRING_PROFILE_TEST)
public class CassandraConfigurationIT extends CassandraConfiguration {

    /**
     * Override how to get the port to connect to the Cassandra cluster.
     * <p>
     * This uses the TestContainers API to get the mapped port in Docker.
     */
    @Override
    protected int getPort(CassandraProperties properties) {
        return AbstractCassandraTest.CASSANDRA_CONTAINER.getMappedPort(AbstractCassandraTest.CASSANDRA_TEST_PORT);
    }
}
