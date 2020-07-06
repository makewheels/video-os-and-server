package com.eg.videoosandserver.video;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class VideoService {
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
    public void add(String videoId, String m3u8FileUrl, int tsAmount,
                    String videoFileFullName, String videoFileBaseName, String videoFileExtension) {
        Video video = new Video();
        video.setCreateTime(new Date());
        video.setVideoId(videoId);
        video.setM3u8FileUrl(m3u8FileUrl);
        video.setTsAmount(tsAmount);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        videoDao.save(video);
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
