package io.github.jhipster.sample;

import io.github.jhipster.sample.JhipsterCassandraSampleApplicationApp;
import io.github.jhipster.sample.config.AsyncSyncConfiguration;
import io.github.jhipster.sample.config.EmbeddedCassandra;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { JhipsterCassandraSampleApplicationApp.class, AsyncSyncConfiguration.class })
@EmbeddedCassandra
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public @interface IntegrationTest {
}
