package com.eg.videoosandserver.viewlog;

import com.alibaba.fastjson.JSONObject;
import com.eg.videoosandserver.video.Video;
import com.eg.videoosandserver.video.VideoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.lang.invoke.VolatileCallSite;
import java.util.Date;

@Service
@Transactional
public class ViewLogService {
    @Resource
    private VideoDao videoDao;
    @Resource
    private ViewLogDao viewLogDao;

    /**
     * 保存新的观看记录
     */
    public void handleNewViewLog(Video video, String ip, JSONObject ipJson, String userAgent) {
        String videoId = video.getVideoId();
        int newViewCount = video.getViewCount() + 1;

        //解析ip位置json
        JSONObject result = ipJson.getJSONObject("result");
        String province = result.getString("province");
        String city = result.getString("city");
        String district = result.getString("district");

        //保存新的viewLog记录
        ViewLog viewLog = new ViewLog();
        viewLog.setVideoId(videoId);

        viewLog.setIp(ip);
        viewLog.setIpJson(ipJson.toJSONString());
        viewLog.setIpProvince(province);
        viewLog.setIpCity(city);
        viewLog.setIpDistrict(district);

        viewLog.setUserAgent(userAgent);
        viewLog.setViewTime(new Date());
        viewLog.setNumberOfThisVideo(newViewCount);
        viewLogDao.save(viewLog);

        //更新video
        video.setViewCount(newViewCount);
        videoDao.save(video);
    }
}
