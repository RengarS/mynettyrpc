package com.aries.server;


import com.aries.server.annotation.BasePackage;
import com.aries.server.annotation.RpcServer;

public class AriesRpcFrameWork {

    private static String classPath;
    private static int port;

    public static void run(Class<?> clz) {

        if (clz.isAnnotationPresent(BasePackage.class)) {
            BasePackage basePackage = clz.getDeclaredAnnotation(BasePackage.class);
            classPath = basePackage.value();
        }

        if (clz.isAnnotationPresent(RpcServer.class)) {
            RpcServer rpcServer = clz.getDeclaredAnnotation(RpcServer.class);
            port = rpcServer.port();
        }

    }

    public static String getClassPath() {
        return
                classPath;
    }

    public static int getPort() {
        return
                port;
    }
}
