package com.eg.videoosandserver.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class IpUtil {
    public static JSONObject getLocationByIp(String ip) {
        String response = HttpUtil.createGet("https://ips.market.alicloudapi.com/iplocaltion?ip=" + ip)
                .header("Authorization", "APPCODE d782d853d6f3461293a1d1b1ad9edbf3")
                .execute().body();
        return JSON.parseObject(response);
    }

}
