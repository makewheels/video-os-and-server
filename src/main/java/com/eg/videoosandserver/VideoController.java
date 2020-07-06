package com.eg.videoosandserver;

import com.eg.videoosandserver.util.Contants;
import com.eg.videoosandserver.video.Video;
import com.eg.videoosandserver.video.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/video")
public class VideoController {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.port}")
    private int port;
    @Resource
    private VideoService videoService;

    /**
     * 新增视频
     *
     * @param password
     * @param videoId
     * @param m3u8FileUrl
     * @param tsAmount
     * @param videoFileFullName
     * @param videoFileBaseName
     * @param videoFileExtension
     * @return
     */
    @RequestMapping("/notifyNewVideo")
    @ResponseBody
    public String notifyNewVideo(@Param("password") String password,
                                 @Param("videoId") String videoId,
                                 @Param("m3u8FileUrl") String m3u8FileUrl,
                                 @Param("tsAmount") int tsAmount,
                                 @Param("videoFileFullName") String videoFileFullName,
                                 @Param("videoFileBaseName") String videoFileBaseName,
                                 @Param("videoFileExtension") String videoFileExtension) {
        //校验密码
        if (StringUtils.isEmpty(password) || !password.equals("N9Q0HsaSniSNiQ94")) {
            return null;
        }
        //新增记录
        videoService.add(videoId, m3u8FileUrl, tsAmount,
                videoFileFullName, videoFileBaseName, videoFileExtension);
        return "http://" + Contants.IP + ":" + port + contextPath
                + "/video/watch?videoId=" + videoId;
    }

    /**
     * 看视频
     *
     * @param videoId
     * @return
     */
    @RequestMapping("/watch")
    public String watch(@Param("videoId") String videoId, Map<String, String> map) {
        Video video = videoService.getVideoByVideoId(videoId);
        map.put("title", video.getVideoFileBaseName());
        map.put("videoSourceUrl", video.getM3u8FileUrl());
        return "watch";
    }

}
