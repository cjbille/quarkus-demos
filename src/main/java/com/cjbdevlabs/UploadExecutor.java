package com.cjbdevlabs;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class UploadExecutor {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public ExecutorService get() {
        return executor;
    }

    @PreDestroy
    void shutdown() {
        executor.shutdown();
    }
}

