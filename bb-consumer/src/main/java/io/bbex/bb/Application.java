package io.bbex.bb;

import com.lambdaworks.redis.codec.StringCodec;
import com.zaxxer.hikari.metrics.prometheus.PrometheusHistogramMetricsTrackerFactory;
import io.bbex.base.redis.cluster.CacheClusterClient;
import io.bbex.bb.server.cache.CacheClusterProperties;
import io.bbex.bb.server.config.GrpcClientProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@Slf4j
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"io.bbex.bb.server", "io.bbex.bb.common", "io.bbex.bb.service", "io.bbex.base", "io.bbex.ex", "io.bbex.bb.schedule"})
public class Application {

    public static final String ALERT = "[ALERT]";

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("TaskScheduler-");
        scheduler.setAwaitTerminationSeconds(10);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    @Bean
    public GrpcClientProperties grpcClientProperties() {
        return new GrpcClientProperties();
    }

    @Bean
    public CacheClusterProperties cacheClusterProperties() {
        return new CacheClusterProperties();
    }

    @Bean
    public CacheClusterClient cacheClusterClient(CacheClusterProperties cacheClusterProperties) {
        return new CacheClusterClient(cacheClusterProperties.getShards(), StringCodec.UTF8);
    }

    @Bean
    public PrometheusHistogramMetricsTrackerFactory prometheusHistogramMetricsTrackerFactory() {
        return new PrometheusHistogramMetricsTrackerFactory();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

