package io.github.jhipster.sample;

import static java.nio.file.Files.newDirectoryStream;
import static java.nio.file.Paths.get;
import static java.util.Spliterator.SORTED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.StreamSupport.stream;

import com.datastax.oss.driver.api.core.CqlSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Iterator;
import java.util.Spliterator;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import tech.jhipster.config.JHipsterConstants;

/**
 * Base class for starting/stopping Cassandra during tests.
 */
@ActiveProfiles(JHipsterConstants.SPRING_PROFILE_TEST)
@ContextConfiguration(initializers = AbstractCassandraTest.CassandraPortInitializer.class)
public class AbstractCassandraTest {

    public static final String CASSANDRA_UNIT_KEYSPACE = "cassandra_unit_keyspace";
    public static final GenericContainer<?> CASSANDRA_CONTAINER;
    public static final int CASSANDRA_TEST_PORT = 9042;
    private static boolean started = false;

    static {
        CASSANDRA_CONTAINER =
            new GenericContainer("cassandra:3.11.10")
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                .withExposedPorts(CASSANDRA_TEST_PORT);
    }

    @BeforeAll
    public static void startServer() throws IOException, URISyntaxException {
        if (!started) {
            startTestContainer();

            CqlSession session = CqlSession
                .builder()
                .addContactPoint(
                    new InetSocketAddress(
                        CASSANDRA_CONTAINER.getContainerIpAddress(),
                        CASSANDRA_CONTAINER.getMappedPort(CASSANDRA_TEST_PORT)
                    )
                )
                .withLocalDatacenter("datacenter1")
                .build();

            createTestKeyspace(session);
            CQLDataLoader dataLoader = new CQLDataLoader(session);
            applyScripts(dataLoader, "config/cql/changelog/", "*.cql");
            started = true;
        }
    }

    private static void startTestContainer() {
        CASSANDRA_CONTAINER.start();
    }

    private static void createTestKeyspace(CqlSession session) {
        String createQuery =
            "CREATE KEYSPACE " + CASSANDRA_UNIT_KEYSPACE + " WITH replication={'class' : 'SimpleStrategy', 'replication_factor':1}";
        session.execute(createQuery);
        String useKeyspaceQuery = "USE " + CASSANDRA_UNIT_KEYSPACE;
        session.execute(useKeyspaceQuery);
    }

    private static void applyScripts(CQLDataLoader dataLoader, String cqlDir, String pattern) throws IOException, URISyntaxException {
        URL dirUrl = ClassLoader.getSystemResource(cqlDir);
        if (dirUrl == null) { // protect for empty directory
            return;
        }

        Iterator<Path> pathIterator = newDirectoryStream(get(dirUrl.toURI()), pattern).iterator();
        Spliterator<Path> pathSpliterator = spliteratorUnknownSize(pathIterator, SORTED);
        stream(pathSpliterator, false)
            .map(Path::getFileName)
            .map(Path::toString)
            .sorted()
            .map(file -> cqlDir + file)
            .map(dataSetLocation -> new ClassPathCQLDataSet(dataSetLocation, false, false, CASSANDRA_UNIT_KEYSPACE))
            .forEach(dataLoader::load);
    }

    public static class CassandraPortInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.data.cassandra.port=" +
                AbstractCassandraTest.CASSANDRA_CONTAINER.getMappedPort(AbstractCassandraTest.CASSANDRA_TEST_PORT)
            );
        }
    }
}
