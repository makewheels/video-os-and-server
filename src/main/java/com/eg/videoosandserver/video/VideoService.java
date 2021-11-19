package com.eg.videoosandserver.video;

import com.eg.videoosandserver.util.Constants;
import com.eg.videoosandserver.util.DingDingUtil;
import com.eg.videoosandserver.util.UserAgentParser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class VideoService {
//    @Value("${server.servlet.context-path}")
//    private String contextPath;
//    @Value("${server.port}")
//    private int port;

    @Resource
    private VideoDao videoDao;

    private String newHlsVideo(
            String videoId, String type, String playFileUrl,
            String m3u8FileUrl, int tsAmount,
            String videoFileFullName, String videoFileBaseName,
            String videoFileExtension) {
        Video video = new Video();
        video.setViewCount(0);
        video.setCreateTime(new Date());
        video.setVideoId(videoId);
        video.setM3u8_file_url(m3u8FileUrl);
        video.setTsAmount(tsAmount);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        String watchUrl = Constants.BASE_URL + "/watch?v=" + videoId;
        video.setWatchUrl(watchUrl);
        videoDao.save(video);
        return watchUrl;
    }

    private String newWebmVideo(
            String videoId, String type, String playFileUrl,
            String videoFileFullName, String videoFileBaseName,
            String videoFileExtension) {
        Video video = new Video();
        video.setViewCount(0);
        video.setCreateTime(new Date());
        video.setVideoId(videoId);
        video.setPlayFileUrl(playFileUrl);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        String watchUrl = Constants.BASE_URL + "/watch?v=" + videoId;
        video.setWatchUrl(watchUrl);
        videoDao.save(video);
        return watchUrl;
    }

    public static void main(String[] args) {

    }

    /**
     * 新增记录
     */
    public String add(
            String videoId, String type, String playFileUrl,
            String m3u8FileUrl, int tsAmount,
            String videoFileFullName, String videoFileBaseName,
            String videoFileExtension) {
        if (type.equals(Constants.TYPE_HLS)) {
            return newHlsVideo(videoId, type, playFileUrl, m3u8FileUrl, tsAmount, videoFileFullName,
                    videoFileBaseName, videoFileExtension);
        } else if (type.equals(Constants.TYPE_WEBM)) {
            return newWebmVideo(videoId, type, playFileUrl, videoFileFullName, videoFileBaseName,
                    videoFileExtension);
        }
        return null;
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
                            "# NetType: " + UserAgentParser.getNetType(userAgentString);
            DingDingUtil.sendMarkdown(markdownText);
        }).start();
    }

}
