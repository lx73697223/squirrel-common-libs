package com.pi.common.autoconfigure.quartz;

import com.pi.common.utils.spring.Profiles;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "quartz")
@ConditionalOnResource(resources = "classpath:quartz.properties")
public class QuartzAutoConfiguration {

    private ResourceLoader resourceLoader;

    @Setter
    private boolean autoStartup = true;

    public QuartzAutoConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public AutowireCapableSpringBeanJobFactory jobFactory() {
        return new AutowireCapableSpringBeanJobFactory();
    }

    @Bean
    public ThreadPoolExecutorFactoryBean quartzTaskExecutor() {

        ThreadPoolExecutorFactoryBean executorFactoryBean = new ThreadPoolExecutorFactoryBean();
        executorFactoryBean.setCorePoolSize(6);
        executorFactoryBean.setMaxPoolSize(6);
        executorFactoryBean.setQueueCapacity(0);
        executorFactoryBean.setWaitForTasksToCompleteOnShutdown(true);
        executorFactoryBean.setAwaitTerminationSeconds(60);
        executorFactoryBean.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executorFactoryBean;
    }

    @Bean
    @ConditionalOnBean(Trigger.class)
    public SchedulerFactoryBean scheduler(Trigger[] triggers, DataSource dataSource) {

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory());
        schedulerFactoryBean.setTriggers(triggers);
        schedulerFactoryBean.setDataSource(dataSource);

        String profile = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
        String propertiesName = "quartz.properties";
        if (StringUtils.isNotBlank(profile) && !profile.equals(Profiles.DEFAULT)) {
            propertiesName = "quartz-" + profile + ".properties";
        }

        schedulerFactoryBean.setConfigLocation(resourceLoader.getResource("classpath:" + propertiesName));
        schedulerFactoryBean.setResourceLoader(resourceLoader);
        schedulerFactoryBean.setTaskExecutor(quartzTaskExecutor().getObject());
        schedulerFactoryBean.setAutoStartup(autoStartup);

        return schedulerFactoryBean;
    }
}
