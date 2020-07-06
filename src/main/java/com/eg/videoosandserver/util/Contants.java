package com.eg.videoosandserver.util;

public class Contants {
    public static String IP;

    static {
        String property = System.getProperty("os.name");
        if (property.toLowerCase().startsWith("win")) {
            IP = "localhost";
        } else {
            IP = "baidu.server.qbserver.cn";
        }
    }
}
