package io.github.jhipster.sample.config;

import java.util.List;
import org.cassandraunit.CQLDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.testcontainers.containers.CassandraContainer;

public class TestContainersSpringContextCustomizerFactory implements ContextCustomizerFactory {

    private Logger log = LoggerFactory.getLogger(TestContainersSpringContextCustomizerFactory.class);
    private static CassandraTestContainer cassandraBean;

    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        return (context, mergedConfig) -> {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            TestPropertyValues testValues = TestPropertyValues.empty();
            EmbeddedCassandra cassandraAnnotation = AnnotatedElementUtils.findMergedAnnotation(testClass, EmbeddedCassandra.class);
            if (null != cassandraAnnotation) {
                log.debug("detected the EmbeddedCassandra annotation on class {}", testClass.getName());
                if (cassandraBean == null) {
                    log.info("Warming up the cassandra database");
                    cassandraBean = new CassandraTestContainer();
                    beanFactory.initializeBean(cassandraBean, CassandraTestContainer.class.getName().toLowerCase());
                    beanFactory.registerSingleton(CassandraTestContainer.class.getName().toLowerCase(), cassandraBean);
                    ((DefaultSingletonBeanRegistry) beanFactory).registerDisposableBean(
                            CassandraTestContainer.class.getName().toLowerCase(),
                            cassandraBean
                        );
                }
                testValues =
                    testValues
                        .and(
                            "spring.data.cassandra.port=" + cassandraBean.getCassandraContainer().getMappedPort(CassandraContainer.CQL_PORT)
                        )
                        .and("spring.data.cassandra.contact-points=" + cassandraBean.getCassandraContainer().getHost())
                        .and("spring.data.cassandra.keyspace-name=" + CQLDataLoader.DEFAULT_KEYSPACE_NAME)
                        .and(
                            "spring.data.cassandra.local-datacenter=" +
                            cassandraBean.getCassandraContainer().getCluster().getMetadata().getAllHosts().iterator().next().getDatacenter()
                        )
                        .and(
                            "spring.data.cassandra.cluster-name=" +
                            cassandraBean.getCassandraContainer().getCluster().getMetadata().getClusterName()
                        );
            }

            testValues.applyTo(context);
        };
    }
}
