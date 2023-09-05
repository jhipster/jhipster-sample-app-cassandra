package io.github.jhipster.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import org.cassandraunit.CQLDataLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CassandraKeyspaceIT {

    @Autowired
    private CqlSession session;

    @Test
    void shouldListCassandraUnitKeyspace() {
        Metadata metadata = session.getMetadata();
        assertThat(metadata.getKeyspace(CQLDataLoader.DEFAULT_KEYSPACE_NAME)).isNotNull();
    }
}
