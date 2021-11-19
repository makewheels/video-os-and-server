package com.eg.videoosandserver.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

    public static String getVideoId() {
        return RandomStringUtils.randomAlphanumeric(11);
    }

}
