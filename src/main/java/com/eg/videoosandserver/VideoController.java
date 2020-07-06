package com.eg.videoosandserver;

import com.eg.videoosandserver.util.RandomUtil;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/video")
public class VideoController {

    @RequestMapping("/notifyNewVideo")
    @ResponseBody
    public String notifyNewVideo(@Param("password") String password,
                                 @Param("videoId") String videoId,
                                 @Param("m3u8Url") String m3u8Url,
                                 @Param("tsAmount") String tsAmount) {
        return RandomUtil.getString();
    }

}
