package com.aries.disserver;

import com.aries.disserver.utils.RegisterClientBoot;
import com.aries.disserver.utils.RegisterClientUtil;

public class Test {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                RegisterClientBoot.connect("127.0.0.1", 9999);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RegisterClientUtil.doRegister(7666, "serviceA");

        String url = RegisterClientUtil.getServiceUrl("serviceA");
        System.out.println(url);
    }
}
