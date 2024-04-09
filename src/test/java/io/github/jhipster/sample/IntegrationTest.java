package io.github.jhipster.sample;

import io.github.jhipster.sample.config.AsyncSyncConfiguration;
import io.github.jhipster.sample.config.EmbeddedCassandra;
import io.github.jhipster.sample.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { JhipsterCassandraSampleApplicationApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedCassandra
public @interface IntegrationTest {
}
