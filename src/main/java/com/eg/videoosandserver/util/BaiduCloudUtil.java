package com.eg.videoosandserver.util;

import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;

import java.io.File;

/**
 * 百度云工具类
 */
public class BaiduCloudUtil {
    private static final String ACCESS_KEY_ID = "ace034ef06b040c0b578a668f292f493";
    private static final String SECRET_ACCESS_KEY = "0922593ba25244d19bc7a340188fe57c";
    private static final String ENDPOINT = "bj.bcebos.com";
    private static final String BUCKET_NAME = "video-beijing";
    private static final String BASE_URL = "https://video-beijing.cdn.bcebos.com";

    private static final BosClient client;

    static {
        BosClientConfiguration config = new BosClientConfiguration();
        config.setCredentials(new DefaultBceCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY));
        config.setEndpoint(ENDPOINT);
        client = new BosClient(config);
    }

    /**
     * 上传到对象存储
     *
     * @param file
     * @param objectKey
     * @return
     */
    public static String uploadObjectStorage(File file, String objectKey) {
        client.putObject(BUCKET_NAME, objectKey, file);
        return BASE_URL + objectKey;
    }

}
