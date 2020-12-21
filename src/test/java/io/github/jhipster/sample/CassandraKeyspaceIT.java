package io.github.jhipster.sample;

import static org.assertj.core.api.Assertions.assertThat;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.Metadata;
import io.github.jhipster.sample.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@IntegrationTest
public class CassandraKeyspaceIT extends AbstractCassandraTest {

    @Autowired
    private CqlSession session;

    @Test
    void shouldListCassandraUnitKeyspace() {
        Metadata metadata = session.getMetadata();
        assertThat(metadata.getKeyspace(CASSANDRA_UNIT_KEYSPACE)).isNotNull();
    }
}
