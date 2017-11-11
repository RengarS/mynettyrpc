package com.aries.server;


import com.aries.server.annotation.BasePackage;
import com.aries.server.annotation.RpcServerStart;
import com.aries.server.rpcserver.RpcServer;
import com.aries.server.helper.BeanHelper;
import com.aries.server.helper.ClassHelper;
import com.aries.server.helper.IoCHelper;
import com.aries.server.helper.ServiceHelper;

public class AriesRpcFrameWork {

    private static String packageName;
    private static int port;


    public static void run(Class<?> clz) {

        if (clz.isAnnotationPresent(BasePackage.class)) {
            BasePackage basePackage = clz.getDeclaredAnnotation(BasePackage.class);
            packageName = basePackage.value();
        }

        if (clz.isAnnotationPresent(RpcServerStart.class)) {
            RpcServerStart rpcServer = clz.getDeclaredAnnotation(RpcServerStart.class);
            port = rpcServer.port();
        }
        Thread thread = Thread.currentThread();

        new ClassHelper();
        new BeanHelper();
        new IoCHelper();
        new ServiceHelper();
        new Thread(() ->
        {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                new RpcServer().bind(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static String getPackageName() {
        return
                packageName;
    }

    public static int getPort() {
        return
                port;
    }

}
