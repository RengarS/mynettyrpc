package com.aries.mynettyrpc.consts;

import com.aries.mynettyrpc.domain.RpcResponse1;

import java.util.concurrent.*;

public class ThreadPool {
    /**
     * 发送请求的线程池
     */
    private static final ExecutorService EXECUTOR_SERVICE =
            new ThreadPoolExecutor(5, 8, 20, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(10));

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
}
