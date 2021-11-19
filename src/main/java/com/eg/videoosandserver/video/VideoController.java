package com.eg.videoosandserver.video;

import com.eg.videoosandserver.util.Contants;
import com.eg.videoosandserver.viewlog.ViewLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class VideoController {
    @Resource
    private VideoService videoService;
    @Resource
    private ViewLogService viewLogService;

    @RequestMapping("healthCheck")
    @ResponseBody
    public String test() {
        return "ok";
    }

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
    public String notifyNewVideo(@RequestParam("password") String password,
                                 @RequestParam("videoId") String videoId,
                                 @RequestParam("m3u8FileUrl") String m3u8FileUrl,
                                 @RequestParam("tsAmount") int tsAmount,
                                 @RequestParam("videoFileFullName") String videoFileFullName,
                                 @RequestParam("videoFileBaseName") String videoFileBaseName,
                                 @RequestParam("videoFileExtension") String videoFileExtension) {
        //校验密码
        if (StringUtils.isEmpty(password) || !password.equals(Contants.PASSWORD)) {
            return null;
        }
        //新增记录
        return videoService.add(videoId, m3u8FileUrl, tsAmount,
                videoFileFullName, videoFileBaseName, videoFileExtension);
    }

    /**
     * 看视频
     *
     * @param videoId
     * @return
     */
    @RequestMapping("/watch")
    public String watch(@RequestParam("v") String videoId, Map<String, String> map,
                        HttpServletRequest request) {
        //找到这个视频
        Video video = videoService.getVideoByVideoId(videoId);
        //保存viewLog
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("user-agent");
        viewLogService.handleNewViewLog(video, ip, userAgent);
        //钉钉通知
        videoService.watchNotify(video, ip, userAgent);
        //返回前端页面
        map.put("title", video.getVideoFileBaseName());
        map.put("videoSourceUrl", video.getM3u8FileUrl());
        return "watch";
    }

}
