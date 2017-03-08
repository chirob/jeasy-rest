package com.github.chirob.jeasyrest.concurrent.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadExecutor {

    public static void execute(Runnable task) {
        THREAD_EXECUTOR_SERVICE.execute(task);
    }

    private static final ExecutorService THREAD_EXECUTOR_SERVICE = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r) {
                {
                    setDaemon(true);
                }
            };
        }
    });

}
