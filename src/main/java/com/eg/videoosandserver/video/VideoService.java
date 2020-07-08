package com.eg.videoosandserver.video;

import com.eg.videoosandserver.util.Contants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
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
                    String videoFileFullName, String videoFileBaseName, String videoFileExtension) {
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
        String watchUrl = "http://" + Contants.IP + ":" + port + contextPath
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
}
