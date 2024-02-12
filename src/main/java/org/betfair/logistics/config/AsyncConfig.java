package org.betfair.logistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AsyncConfig {

    @Bean
    public ExecutorService executorService() {
        return  new ThreadPoolExecutor(4, 8,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
    }

    @Bean
    public ExecutorService smallExecutorService() {
        return  new ThreadPoolExecutor(2, 4,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
    }

}
