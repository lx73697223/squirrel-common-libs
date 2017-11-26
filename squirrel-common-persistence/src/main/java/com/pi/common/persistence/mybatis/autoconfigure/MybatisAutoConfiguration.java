package com.pi.common.persistence.mybatis.autoconfigure;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import com.pi.common.persistence.mybatis.dialect.SQLServerDialect;
import com.pi.common.persistence.mybatis.plugins.OffsetLimitInterceptor;
import com.pi.common.persistence.mybatis.plugins.OptimisticLockingInterceptor;
import com.pi.common.persistence.mybatis.typehandle.EnumEntityTypeHandler;
import com.pi.common.persistence.mybatis.typehandle.LocalDateTimeTypeHandler;
import com.pi.common.persistence.mybatis.typehandle.LocalDateTypeHandler;
import com.pi.common.persistence.mybatis.typehandle.LocalTimeTypeHandler;
import com.pi.common.persistence.mybatis.typehandle.UUIDTypeHandler;
import com.pi.common.persistence.mybatis.typehandle.ZonedDateTimeTypeHandler;
import com.pi.common.utils.core.IteratorUtils;
import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityUtils;

@Configuration
@ConditionalOnClass(SqlSessionFactory.class)
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration implements ApplicationListener<ApplicationEvent> {

    private static Logger LOGGER = LoggerFactory.getLogger(MybatisAutoConfiguration.class);

    @Autowired
    private MybatisProperties properties;

    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private SqlSessionFactory sqlSessionFactory;

    /**
     This will just scan the same base package as Spring Boot does.
     If you want more power, you can explicitly use {@link org.mybatis.spring.annotation.MapperScan}
     but this will get typed mappers working correctly, out-of-the-box, similar to using Spring Data JPA repositories.
     */
    public static class AutoConfiguredMapperScannerRegistrar
            implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;

        private ResourceLoader resourceLoader;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

            ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

            List<String> pkgs;
            try {
                pkgs = AutoConfigurationPackages.get(beanFactory);
                for (String pkg : pkgs) {
                    LOGGER.debug("Found MyBatis auto-configuration package '" + pkg + "'");
                }

                if (resourceLoader != null) {
                    scanner.setResourceLoader(resourceLoader);
                }

                scanner.registerFilters();
                scanner.doScan(pkgs.toArray(new String[pkgs.size()]));
            } catch (IllegalStateException ex) {
                LOGGER.debug("Could not determine auto-configuration " + "package, automatic mapper scanning disabled.");
            }
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory)
                throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    /**
     {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating instances of {@link MapperFactoryBean}. If {@link
    org.mybatis.spring.annotation.MapperScan} is used then this auto-configuration is not needed. If it is _not_ used, however,
     then this will bring in a bean registrar and automatically register components based on the same component-scanning path as
     Spring Boot itself.
     */
    @Configuration
    @Import({ AutoConfiguredMapperScannerRegistrar.class })
    @ConditionalOnMissingBean(MapperFactoryBean.class)
    public static class MapperScannerRegistrarNotFoundConfiguration {

        @PostConstruct
        public void afterPropertiesSet() {
            LOGGER.debug(String.format("No %s found.", MapperFactoryBean.class.getName()));
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            // fail-fast -> check all statements are completed
            sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource)
            throws Exception {

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        XMLConfigBuilder xmlConfigBuilder = null;
        if (properties.getConfig() != null) {
            try (InputStream inputStream = resourceLoader.getResource(properties.getConfig()).getInputStream()) {
                xmlConfigBuilder = new XMLConfigBuilder(inputStream, null, null);
                configuration = xmlConfigBuilder.getConfiguration();
            }
        } else {
            configuration = new org.apache.ibatis.session.Configuration();
        }

        if (ArrayUtils.isNotEmpty(properties.getTypeAliasesPackages())) {
            for (String packageToScan : properties.getTypeAliasesPackages()) {
                configuration.getTypeAliasRegistry().registerAliases(packageToScan, Object.class);
            }
        }

        if (properties.isPaginationEnabled()) {
            OffsetLimitInterceptor offsetLimitInterceptor = new OffsetLimitInterceptor();
            offsetLimitInterceptor.setDialect(new SQLServerDialect());
            configuration.addInterceptor(offsetLimitInterceptor);
        }
        configuration.addInterceptor(new OptimisticLockingInterceptor());

        if (ArrayUtils.isNotEmpty(properties.getTypeHandlersPackages())) {
            for (String packageToScan : properties.getTypeHandlersPackages()) {
                configuration.getTypeHandlerRegistry().register(packageToScan);
            }
        }

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

        EnumEntityUtils.scanEnumEntityPackages(type -> {
            typeHandlerRegistry.register((Class<EnumEntity<Object>>) type,
                    EnumEntityTypeHandler.create(typeHandlerRegistry, type));
        }, properties.getEnumEntityPackages());

        typeHandlerRegistry.register(new LocalDateTimeTypeHandler());
        typeHandlerRegistry.register(new LocalDateTypeHandler());
        typeHandlerRegistry.register(new LocalTimeTypeHandler());
        typeHandlerRegistry.register(new ZonedDateTimeTypeHandler());
        typeHandlerRegistry.register(new UUIDTypeHandler());

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + properties.getConfig(), ex);
            } finally {
                ErrorContext.instance().reset();
            }
        } else {
            configuration.setCacheEnabled(properties.isCacheEnabled());
            configuration.setUseGeneratedKeys(properties.isUseGeneratedKeys());
            configuration.setDefaultExecutorType(properties.getExecutorType());
            configuration.setMapUnderscoreToCamelCase(properties.isMapUnderscoreToCamelCase());
            configuration.setLogImpl(configuration.getTypeAliasRegistry().resolveAlias(properties.getLogImpl()));
        }

        configuration
                .setEnvironment(new Environment(getClass().getSimpleName(), new SpringManagedTransactionFactory(), dataSource));

        Resource databaseIdProviderResource = resourceLoader.getResource("classpath:databaseIdProvider.properties");
        if (databaseIdProviderResource.exists()) {
            Properties properties = new Properties();

            try (InputStream input = databaseIdProviderResource.getInputStream()) {
                properties.load(input);
            }

            DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
            databaseIdProvider.setProperties(properties);
            configuration.setDatabaseId(databaseIdProvider.getDatabaseId(dataSource));
        }

        if (IteratorUtils.isEmpty(properties.getMapperLocations())) {
            throw new IllegalArgumentException("Mapper locations of Mybatis configuration can not be empty");
        }

        for (Resource mapperLocation : properties.getMapperLocations()) {
            if (mapperLocation == null) {
                continue;
            }

            try {
                XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration,
                                                                                mapperLocation.toString(),
                                                                                configuration.getSqlFragments());
                xmlMapperBuilder.parse();
            } catch (Exception e) {
                throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);

        return sqlSessionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
