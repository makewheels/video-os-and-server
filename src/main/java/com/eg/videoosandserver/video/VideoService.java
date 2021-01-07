package com.eg.videoosandserver.video;

import com.eg.videoosandserver.util.Contants;
import com.eg.videoosandserver.util.DingDingUtil;
import com.eg.videoosandserver.util.UserAgentParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

@Service
@Transactional
public class VideoService {
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${server.port}")
    private int port;

    @Resource
    private VideoDao videoDao;

    /**
     * 新增记录
     *
     * @param videoId
     * @param m3u8FileUrl
     * @param tsAmount
     * @param videoFileFullName
     * @param videoFileBaseName
     * @param videoFileExtension
     */
    public String add(String videoId, String m3u8FileUrl, int tsAmount,
                      String videoFileFullName, String videoFileBaseName,
                      String videoFileExtension) {
        Video video = new Video();
        //初始设置观看数为零
        video.setViewCount(0);
        video.setCreateTime(new Date());
        video.setVideoId(videoId);
        video.setM3u8FileUrl(m3u8FileUrl);
        video.setTsAmount(tsAmount);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        String watchUrl = "https://" + Contants.IP + ":" + port + contextPath
                + "/video/watch?videoId=" + videoId;
        video.setWatchUrl(watchUrl);
        videoDao.save(video);
        return watchUrl;
    }

    /**
     * 根据id找视频
     *
     * @param videoId
     * @return
     */
    public Video getVideoByVideoId(String videoId) {
        return videoDao.findByVideoId(videoId);
    }

    /**
     * 看视频通知
     *
     * @param video
     * @param ip
     * @param userAgentString
     */
    public void watchNotify(Video video, String ip, String userAgentString) {
        new Thread(() -> {
            String markdownText =
                    "# video: " + video.getVideoFileBaseName() + "\n\n" +
                            "# viewCount: " + video.getViewCount() + "\n\n" +
                            "# ip: " + ip + "\n\n" +
                            "# deviceInfo: " + UserAgentParser.getDeviceInfo(userAgentString) + "\n\n" +
                            "# NetType: " + UserAgentParser.getNetType(userAgentString) + "\n\n" +
                            "# userAgentString: " + userAgentString;
            DingDingUtil.sendMarkdown(markdownText);
        }).start();
    }

    public static void main(String[] args) throws IOException {
        String ua = "Mozilla/5.0 (Linux; Android 9; PCEM00 Build/PPR1.180610.011; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045330 Mobile Safari/537.36 MMWEBID/5582 MicroMessenger/7.0.16.1700(0x2700103E) Process/tools WeChat/arm32 NetType/4G Language/zh_CN ABI/arm64";
        String ua2 = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.15(0x17000f29) NetType/4G Language/zh_CN";
    }
}
