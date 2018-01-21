package com.aries.commons.utils;


import java.util.UUID;

public class IDGenerator {

    public static String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
