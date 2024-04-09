package io.github.jhipster.sample.config;

import static io.github.jhipster.sample.config.CassandraTestContainer.DEFAULT_KEYSPACE_NAME;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.MergedContextConfiguration;
import org.testcontainers.containers.CassandraContainer;

public class CassandraTestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private Logger log = LoggerFactory.getLogger(CassandraTestContainersSpringContextCustomizerFactory.class);

    private static CassandraTestContainer cassandraBean;

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return new ContextCustomizer() {
            @Override
            public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
                ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
                TestPropertyValues testValues = TestPropertyValues.empty();
                EmbeddedCassandra cassandraAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedCassandra.class);
                if (null != cassandraAnnotation) {
                    log.debug("detected the EmbeddedCassandra annotation on class {}", testClass.getName());
                    log.info("Warming up the cassandra database");
                    if (null == cassandraBean) {
                        cassandraBean = beanFactory.createBean(CassandraTestContainer.class);
                        beanFactory.registerSingleton(CassandraTestContainer.class.getName(), cassandraBean);
                        // ((DefaultListableBeanFactory)beanFactory).registerDisposableBean(CassandraTestContainer.class.getName(), cassandraBean);
                    }
                    testValues = testValues
                        .and("spring.cassandra.port=" + cassandraBean.getCassandraContainer().getMappedPort(CassandraContainer.CQL_PORT))
                        .and("spring.cassandra.contact-points=" + cassandraBean.getCassandraContainer().getHost())
                        .and("spring.cassandra.keyspace-name=" + DEFAULT_KEYSPACE_NAME)
                        .and(
                            "spring.cassandra.local-datacenter=" +
                            cassandraBean.getCassandraContainer().getCluster().getMetadata().getAllHosts().iterator().next().getDatacenter()
                        )
                        .and(
                            "spring.cassandra.session-name=" +
                            cassandraBean.getCassandraContainer().getCluster().getMetadata().getClusterName()
                        );
                }
                testValues.applyTo(context);
            }

            @Override
            public int hashCode() {
                return CassandraTestContainer.class.getName().hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return this.hashCode() == obj.hashCode();
            }
        };
    }
}
