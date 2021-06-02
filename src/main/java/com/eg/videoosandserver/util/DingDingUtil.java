package com.eg.videoosandserver.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 钉钉机器人工具类
 *
 * @time 2020-03-16 18:22
 */
public class DingDingUtil {
    private final static String webhookUrl = "https://oapi.dingtalk.com/robot/send" +
            "?access_token=3e7801c290ef27a1609c1b8ccbc4e68f8b64e173f95a8904278c36f1b2e7d34c";
    private final static String secret
            = "SEC16190d470e6d75551d283cc4d7cc0223c22ae8d0e0092c1fd824739ecd88a9ee";

    private static String getUrl() {
        long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret;
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            String sign = URLEncoder.encode(new String(new Base64().encode(signData)), "UTF-8");
            return webhookUrl + "&timestamp=" + timestamp + "&sign=" + sign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送 markdown text
     *
     * @param markdownText
     * @return
     */
    public static OapiRobotSendResponse sendMarkdown(String markdownText) {
        DingTalkClient client = new DefaultDingTalkClient(getUrl());
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("title");
        markdown.setText(markdownText);
        request.setMarkdown(markdown);
        try {
            return client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String text = UUID.randomUUID() + "\n\n" + "fefg";
        System.out.println(text);
        OapiRobotSendResponse response = sendMarkdown(text);
        if (response != null) {
            System.out.println(response.isSuccess());
            System.out.println(response.getErrmsg());
        }
    }
}
