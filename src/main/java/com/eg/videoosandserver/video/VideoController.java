package com.eg.videoosandserver.video;

import com.alibaba.fastjson.JSON;
import com.eg.videoosandserver.util.Constants;
import com.eg.videoosandserver.viewlog.ViewLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
public class VideoController {
    @Resource
    private VideoService videoService;
    @Resource
    private ViewLogService viewLogService;

    @RequestMapping("healthCheck")
    @ResponseBody
    public String healthCheck() {
        String str = "healthCheck response: ok " + System.currentTimeMillis() + " " + UUID.randomUUID();
        System.out.println(str);
        return str;
    }

    /**
     * 新增视频
     */
    @RequestMapping("/notifyNewVideo")
    @ResponseBody
    public String notifyNewVideo(@RequestParam("password") String password,
                                 @RequestParam("videoId") String videoId,
                                 @RequestParam("type") String type,
                                 @RequestParam("playFileUrl") String playFileUrl,

                                 @RequestParam("m3u8FileUrl") String m3u8FileUrl,
                                 @RequestParam("tsAmount") int tsAmount,
                                 @RequestParam("videoFileFullName") String videoFileFullName,
                                 @RequestParam("videoFileBaseName") String videoFileBaseName,
                                 @RequestParam("videoFileExtension") String videoFileExtension,

                                 HttpServletResponse response) {
        //校验密码
        if (StringUtils.isEmpty(password) || !password.equals(Constants.PASSWORD)) {
            response.setStatus(HttpStatus.SC_FORBIDDEN);
            return null;
        }
        //新增记录
        return videoService.add(videoId, type, playFileUrl, m3u8FileUrl, tsAmount,
                videoFileFullName, videoFileBaseName, videoFileExtension);
    }

    /**
     * 看视频
     */
    @RequestMapping("/watch")
    public String watch(@RequestParam("v") String videoId, Map<String, String> map,
                        HttpServletRequest request) {
        Video video = videoService.handleWatchVideo(request, videoId);
        log.info(JSON.toJSONString(video));
        //返回前端页面
        map.put("title", video.getVideoFileBaseName());
        map.put("videoSourceUrl", video.getM3u8_file_url());
        return "watch";
    }

}
