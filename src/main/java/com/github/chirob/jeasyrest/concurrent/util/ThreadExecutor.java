package com.github.chirob.jeasyrest.concurrent.util;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.MDC;

public class ThreadExecutor {

    public static void execute(Runnable task) {
        THREAD_EXECUTOR_SERVICE.execute(new MDCContextAwareTask(task));
    }

    private static final ExecutorService THREAD_EXECUTOR_SERVICE = Executors.newCachedThreadPool(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r) {
                {
                    setDaemon(true);
                }
            };
        }
    });

    private static class MDCContextAwareTask implements Runnable {
        @Override
        public void run() {
            if (mdcContextMap != null) {
                MDC.setContextMap(mdcContextMap);
            }
            task.run();
        }

        private MDCContextAwareTask(Runnable task) {
            mdcContextMap = MDC.getCopyOfContextMap();
            this.task = task;
        }

        private Runnable task;
        private Map<String, String> mdcContextMap;
    }
}
