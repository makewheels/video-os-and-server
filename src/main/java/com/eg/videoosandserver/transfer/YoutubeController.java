package com.eg.videoosandserver.transfer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("youtube")
public class YoutubeController {
    @Resource
    private YoutubeService youtubeService;

    /**
     * 提交新视频
     */
    @RequestMapping("/submitNewVideoMission")
    @ResponseBody
    public String submitNewVideoMission(@RequestParam("youtubeUrl") String youtubeUrl) {
        return youtubeService.submitNewVideoMission(youtubeUrl);
    }
}
