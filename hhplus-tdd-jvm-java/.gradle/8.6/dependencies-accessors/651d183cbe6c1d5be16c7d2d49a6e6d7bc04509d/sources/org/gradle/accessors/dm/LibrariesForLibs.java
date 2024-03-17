package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final MicrometerLibraryAccessors laccForMicrometerLibraryAccessors = new MicrometerLibraryAccessors(owner);
    private final MysqlLibraryAccessors laccForMysqlLibraryAccessors = new MysqlLibraryAccessors(owner);
    private final SpringLibraryAccessors laccForSpringLibraryAccessors = new SpringLibraryAccessors(owner);
    private final TestLibraryAccessors laccForTestLibraryAccessors = new TestLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>assertj</b> with <b>org.assertj:assertj-core</b> coordinates and
     * with version reference <b>assertj</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getAssertj() {
        return create("assertj");
    }

    /**
     * Dependency provider for <b>h2</b> with <b>com.h2database:h2</b> coordinates and
     * with <b>no version specified</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getH2() {
        return create("h2");
    }

    /**
     * Dependency provider for <b>lombok</b> with <b>org.projectlombok:lombok</b> coordinates and
     * with <b>no version specified</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getLombok() {
        return create("lombok");
    }

    /**
     * Group of libraries at <b>micrometer</b>
     */
    public MicrometerLibraryAccessors getMicrometer() {
        return laccForMicrometerLibraryAccessors;
    }

    /**
     * Group of libraries at <b>mysql</b>
     */
    public MysqlLibraryAccessors getMysql() {
        return laccForMysqlLibraryAccessors;
    }

    /**
     * Group of libraries at <b>spring</b>
     */
    public SpringLibraryAccessors getSpring() {
        return laccForSpringLibraryAccessors;
    }

    /**
     * Group of libraries at <b>test</b>
     */
    public TestLibraryAccessors getTest() {
        return laccForTestLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class MicrometerLibraryAccessors extends SubDependencyFactory {
        private final MicrometerRegistryLibraryAccessors laccForMicrometerRegistryLibraryAccessors = new MicrometerRegistryLibraryAccessors(owner);
        private final MicrometerTracingLibraryAccessors laccForMicrometerTracingLibraryAccessors = new MicrometerTracingLibraryAccessors(owner);

        public MicrometerLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>micrometer.registry</b>
         */
        public MicrometerRegistryLibraryAccessors getRegistry() {
            return laccForMicrometerRegistryLibraryAccessors;
        }

        /**
         * Group of libraries at <b>micrometer.tracing</b>
         */
        public MicrometerTracingLibraryAccessors getTracing() {
            return laccForMicrometerTracingLibraryAccessors;
        }

    }

    public static class MicrometerRegistryLibraryAccessors extends SubDependencyFactory {

        public MicrometerRegistryLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>prometheus</b> with <b>io.micrometer:micrometer-registry-prometheus</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPrometheus() {
            return create("micrometer.registry.prometheus");
        }

    }

    public static class MicrometerTracingLibraryAccessors extends SubDependencyFactory {
        private final MicrometerTracingBridgeLibraryAccessors laccForMicrometerTracingBridgeLibraryAccessors = new MicrometerTracingBridgeLibraryAccessors(owner);

        public MicrometerTracingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>micrometer.tracing.bridge</b>
         */
        public MicrometerTracingBridgeLibraryAccessors getBridge() {
            return laccForMicrometerTracingBridgeLibraryAccessors;
        }

    }

    public static class MicrometerTracingBridgeLibraryAccessors extends SubDependencyFactory {

        public MicrometerTracingBridgeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>brave</b> with <b>io.micrometer:micrometer-tracing-bridge-brave</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBrave() {
            return create("micrometer.tracing.bridge.brave");
        }

    }

    public static class MysqlLibraryAccessors extends SubDependencyFactory {

        public MysqlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>connector</b> with <b>com.mysql:mysql-connector-j</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getConnector() {
            return create("mysql.connector");
        }

    }

    public static class SpringLibraryAccessors extends SubDependencyFactory {
        private final SpringBootLibraryAccessors laccForSpringBootLibraryAccessors = new SpringBootLibraryAccessors(owner);
        private final SpringCloudLibraryAccessors laccForSpringCloudLibraryAccessors = new SpringCloudLibraryAccessors(owner);

        public SpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>context</b> with <b>org.springframework:spring-context</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getContext() {
            return create("spring.context");
        }

        /**
         * Group of libraries at <b>spring.boot</b>
         */
        public SpringBootLibraryAccessors getBoot() {
            return laccForSpringBootLibraryAccessors;
        }

        /**
         * Group of libraries at <b>spring.cloud</b>
         */
        public SpringCloudLibraryAccessors getCloud() {
            return laccForSpringCloudLibraryAccessors;
        }

    }

    public static class SpringBootLibraryAccessors extends SubDependencyFactory {
        private final SpringBootConfigurationLibraryAccessors laccForSpringBootConfigurationLibraryAccessors = new SpringBootConfigurationLibraryAccessors(owner);
        private final SpringBootStarterLibraryAccessors laccForSpringBootStarterLibraryAccessors = new SpringBootStarterLibraryAccessors(owner);

        public SpringBootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>dependencies</b> with <b>org.springframework.boot:spring-boot-dependencies</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDependencies() {
            return create("spring.boot.dependencies");
        }

        /**
         * Dependency provider for <b>testcontainers</b> with <b>org.springframework.boot:spring-boot-testcontainers</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTestcontainers() {
            return create("spring.boot.testcontainers");
        }

        /**
         * Group of libraries at <b>spring.boot.configuration</b>
         */
        public SpringBootConfigurationLibraryAccessors getConfiguration() {
            return laccForSpringBootConfigurationLibraryAccessors;
        }

        /**
         * Group of libraries at <b>spring.boot.starter</b>
         */
        public SpringBootStarterLibraryAccessors getStarter() {
            return laccForSpringBootStarterLibraryAccessors;
        }

    }

    public static class SpringBootConfigurationLibraryAccessors extends SubDependencyFactory {

        public SpringBootConfigurationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>processor</b> with <b>org.springframework.boot:spring-boot-configuration-processor</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProcessor() {
            return create("spring.boot.configuration.processor");
        }

    }

    public static class SpringBootStarterLibraryAccessors extends SubDependencyFactory {
        private final SpringBootStarterDataLibraryAccessors laccForSpringBootStarterDataLibraryAccessors = new SpringBootStarterDataLibraryAccessors(owner);

        public SpringBootStarterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>actuator</b> with <b>org.springframework.boot:spring-boot-starter-actuator</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getActuator() {
            return create("spring.boot.starter.actuator");
        }

        /**
         * Dependency provider for <b>test</b> with <b>org.springframework.boot:spring-boot-starter-test</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTest() {
            return create("spring.boot.starter.test");
        }

        /**
         * Dependency provider for <b>web</b> with <b>org.springframework.boot:spring-boot-starter-web</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWeb() {
            return create("spring.boot.starter.web");
        }

        /**
         * Group of libraries at <b>spring.boot.starter.data</b>
         */
        public SpringBootStarterDataLibraryAccessors getData() {
            return laccForSpringBootStarterDataLibraryAccessors;
        }

    }

    public static class SpringBootStarterDataLibraryAccessors extends SubDependencyFactory {

        public SpringBootStarterDataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jpa</b> with <b>org.springframework.boot:spring-boot-starter-data-jpa</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJpa() {
            return create("spring.boot.starter.data.jpa");
        }

    }

    public static class SpringCloudLibraryAccessors extends SubDependencyFactory {

        public SpringCloudLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>dependencies</b> with <b>org.springframework.cloud:spring-cloud-dependencies</b> coordinates and
         * with version reference <b>spring.cloud.dependencies</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDependencies() {
            return create("spring.cloud.dependencies");
        }

    }

    public static class TestLibraryAccessors extends SubDependencyFactory {
        private final TestContainersLibraryAccessors laccForTestContainersLibraryAccessors = new TestContainersLibraryAccessors(owner);

        public TestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>test.containers</b>
         */
        public TestContainersLibraryAccessors getContainers() {
            return laccForTestContainersLibraryAccessors;
        }

    }

    public static class TestContainersLibraryAccessors extends SubDependencyFactory {
        private final TestContainersJunitLibraryAccessors laccForTestContainersJunitLibraryAccessors = new TestContainersJunitLibraryAccessors(owner);

        public TestContainersLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>mysql</b> with <b>org.testcontainers:mysql</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMysql() {
            return create("test.containers.mysql");
        }

        /**
         * Group of libraries at <b>test.containers.junit</b>
         */
        public TestContainersJunitLibraryAccessors getJunit() {
            return laccForTestContainersJunitLibraryAccessors;
        }

    }

    public static class TestContainersJunitLibraryAccessors extends SubDependencyFactory {

        public TestContainersJunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jupiter</b> with <b>org.testcontainers:junit-jupiter</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJupiter() {
            return create("test.containers.junit.jupiter");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final SpringVersionAccessors vaccForSpringVersionAccessors = new SpringVersionAccessors(providers, config);
        private final TestVersionAccessors vaccForTestVersionAccessors = new TestVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>assertj</b> with value <b>3.24.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAssertj() { return getVersion("assertj"); }

        /**
         * Version alias <b>junit</b> with value <b>5.9.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("junit"); }

        /**
         * Version alias <b>redisson</b> with value <b>3.25.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRedisson() { return getVersion("redisson"); }

        /**
         * Group of versions at <b>versions.spring</b>
         */
        public SpringVersionAccessors getSpring() {
            return vaccForSpringVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.test</b>
         */
        public TestVersionAccessors getTest() {
            return vaccForTestVersionAccessors;
        }

    }

    public static class SpringVersionAccessors extends VersionFactory  {

        private final SpringCloudVersionAccessors vaccForSpringCloudVersionAccessors = new SpringCloudVersionAccessors(providers, config);
        private final SpringIoVersionAccessors vaccForSpringIoVersionAccessors = new SpringIoVersionAccessors(providers, config);
        public SpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>spring.boot</b> with value <b>3.2.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBoot() { return getVersion("spring.boot"); }

        /**
         * Group of versions at <b>versions.spring.cloud</b>
         */
        public SpringCloudVersionAccessors getCloud() {
            return vaccForSpringCloudVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.spring.io</b>
         */
        public SpringIoVersionAccessors getIo() {
            return vaccForSpringIoVersionAccessors;
        }

    }

    public static class SpringCloudVersionAccessors extends VersionFactory  {

        public SpringCloudVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>spring.cloud.dependencies</b> with value <b>2023.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDependencies() { return getVersion("spring.cloud.dependencies"); }

    }

    public static class SpringIoVersionAccessors extends VersionFactory  {

        private final SpringIoDependencyVersionAccessors vaccForSpringIoDependencyVersionAccessors = new SpringIoDependencyVersionAccessors(providers, config);
        public SpringIoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.spring.io.dependency</b>
         */
        public SpringIoDependencyVersionAccessors getDependency() {
            return vaccForSpringIoDependencyVersionAccessors;
        }

    }

    public static class SpringIoDependencyVersionAccessors extends VersionFactory  {

        public SpringIoDependencyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>spring.io.dependency.management</b> with value <b>1.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getManagement() { return getVersion("spring.io.dependency.management"); }

    }

    public static class TestVersionAccessors extends VersionFactory  {

        public TestVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>test.containers</b> with value <b>1.19.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getContainers() { return getVersion("test.containers"); }

    }

    public static class BundleAccessors extends BundleFactory {
        private final TestcontainersBundleAccessors baccForTestcontainersBundleAccessors = new TestcontainersBundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Group of bundles at <b>bundles.testcontainers</b>
         */
        public TestcontainersBundleAccessors getTestcontainers() {
            return baccForTestcontainersBundleAccessors;
        }

    }

    public static class TestcontainersBundleAccessors extends BundleFactory {

        public TestcontainersBundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

        /**
         * Dependency bundle provider for <b>testcontainers.mysql</b> which contains the following dependencies:
         * <ul>
         *    <li>org.testcontainers:mysql</li>
         *    <li>org.springframework.boot:spring-boot-testcontainers</li>
         *    <li>org.testcontainers:junit-jupiter</li>
         *    <li>org.springframework.boot:spring-boot-starter-test</li>
         * </ul>
         * <p>
         * This bundle was declared in catalog libs.versions.toml
         */
        public Provider<ExternalModuleDependencyBundle> getMysql() {
            return createBundle("testcontainers.mysql");
        }

    }

    public static class PluginAccessors extends PluginFactory {
        private final SpringPluginAccessors paccForSpringPluginAccessors = new SpringPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of plugins at <b>plugins.spring</b>
         */
        public SpringPluginAccessors getSpring() {
            return paccForSpringPluginAccessors;
        }

    }

    public static class SpringPluginAccessors extends PluginFactory {
        private final SpringDependencyPluginAccessors paccForSpringDependencyPluginAccessors = new SpringDependencyPluginAccessors(providers, config);

        public SpringPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>spring.boot</b> with plugin id <b>org.springframework.boot</b> and
         * with version reference <b>spring.boot</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getBoot() { return createPlugin("spring.boot"); }

        /**
         * Group of plugins at <b>plugins.spring.dependency</b>
         */
        public SpringDependencyPluginAccessors getDependency() {
            return paccForSpringDependencyPluginAccessors;
        }

    }

    public static class SpringDependencyPluginAccessors extends PluginFactory {

        public SpringDependencyPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>spring.dependency.management</b> with plugin id <b>io.spring.dependency-management</b> and
         * with version reference <b>spring.io.dependency.management</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getManagement() { return createPlugin("spring.dependency.management"); }

    }

}
