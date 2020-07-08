package com.eg.videoosandserver.video;

import com.eg.videoosandserver.viewlog.ViewLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/video")
public class VideoController {
    @Resource
    private VideoService videoService;
    @Resource
    private ViewLogService viewLogService;

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
        String watchUrl = videoService.add(videoId, m3u8FileUrl, tsAmount,
                videoFileFullName, videoFileBaseName, videoFileExtension);
        return watchUrl;
    }

    /**
     * 看视频
     *
     * @param videoId
     * @return
     */
    @RequestMapping("/watch")
    public String watch(@Param("videoId") String videoId, Map<String, String> map,
                        HttpServletRequest request) {
        //找到这个视频
        Video video = videoService.getVideoByVideoId(videoId);
        //保存viewLog
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("user-agent");
        viewLogService.handleNewViewLog(video, ip, userAgent);
        //返回前端页面
        map.put("title", video.getVideoFileBaseName());
        map.put("videoSourceUrl", video.getM3u8FileUrl());
        return "watch";
    }

}