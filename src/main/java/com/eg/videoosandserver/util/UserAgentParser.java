package com.eg.videoosandserver.util;

import org.apache.commons.lang3.StringUtils;

public class UserAgentParser {

    public static String getDeviceInfo(String userAgent) {
        return StringUtils.substringBetween(userAgent, "(", ")");
    }

    public static String getNetType(String userAgent) {
        String[] array = userAgent.split(" ");
        for (String each : array) {
            if (each.startsWith("NetType")) {
                return each.split("/")[1];
            }
        }
        return null;
    }
}
