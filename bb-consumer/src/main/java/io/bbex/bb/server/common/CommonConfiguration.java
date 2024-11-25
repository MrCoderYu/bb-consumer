package io.bbex.bb.server.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class CommonConfiguration {

    @Bean(name = "confirmTriggerExecutor")
    public ThreadPoolTaskExecutor confirmTriggerExecutor() {
        int corePoolSize = 10;
        int maxPoolSize = 200;
        int queueCapacity = 2000;
        String name = "confirmTrigger-";
        return initExecutor(corePoolSize, maxPoolSize, queueCapacity, name, new ThreadPoolExecutor.CallerRunsPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                super.rejectedExecution(r, executor);
            }
        });
    }

    @Bean(name = "noticeExecutor")
    public ThreadPoolTaskExecutor noticeExecutor() {
        int corePoolSize = 5;
        int maxPoolSize = 5;
        int queueCapacity = 100;
        String name = "notice-";
        return initExecutor(corePoolSize, maxPoolSize, queueCapacity, name, new ThreadPoolExecutor.CallerRunsPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                super.rejectedExecution(r, executor);
            }
        });
    }

    private ThreadPoolTaskExecutor initExecutor(int corePoolSize, int maxPoolSize, int queueCapacity, String name, RejectedExecutionHandler rejectedExecutionHandler) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setKeepAliveSeconds(120);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(name);
        executor.setRejectedExecutionHandler(rejectedExecutionHandler);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

}
