package com.github.jeasyrest.concurrent.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.MDC;

public class ThreadExecutor {

    public static void execute(Runnable task) {
        THREAD_EXECUTOR_SERVICE.execute(new MDCContextAwareTask(task, new RuntimeException()));
    }

    public static Map<Object, Object> threadLocalMap() {
        return TL.get();
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
            TL.set(threadLocalMap);
            try {
                task.run();
            } catch (Exception e) {
                re.initCause(e);
                throw re;
            }
        }

        private MDCContextAwareTask(Runnable task, RuntimeException re) {
            mdcContextMap = MDC.getCopyOfContextMap();
            threadLocalMap = TL.get();
            this.task = task;
            this.re = re;
        }

        private Runnable task;
        private Map<String, String> mdcContextMap;
        private Map<Object, Object> threadLocalMap;
        private RuntimeException re;
    }

    private static final ThreadLocal<Map<Object, Object>> TL = new ThreadLocal<Map<Object, Object>>() {
        @SuppressWarnings("serial")
        @Override
        protected Map<Object, Object> initialValue() {
            return new HashMap<Object, Object>() {
                @Override
                public Object put(Object key, Object value) {
                    if (containsKey(key)) {
                        throw new IllegalArgumentException("Another value has already been set for key='" + key + "'");
                    }
                    return super.put(key, value);
                }
            };
        }
    };

}
