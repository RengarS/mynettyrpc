package com.aries.client.consts;

import com.aries.client.domain.RpcResponse1;

import java.util.concurrent.*;

public class ThreadPool {
    /**
     * 发送请求的线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(6);

    /**
     * 封装submit（）
     *
     * @param callable
     * @return
     */
    public static Future submit(Callable<RpcResponse1> callable) {

        return
                EXECUTOR_SERVICE.submit(callable);
    }

    public static void submit(Runnable task) {
        EXECUTOR_SERVICE.submit(task);
    }
}
