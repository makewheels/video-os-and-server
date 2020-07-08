package com.eg.videoosandserver.viewlog;

import com.eg.videoosandserver.video.Video;
import com.eg.videoosandserver.video.VideoDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class ViewLogService {
    @Resource
    private VideoDao videoDao;
    @Resource
    private ViewLogDao viewLogDao;

    /**
     * 处理来了新的观看记录
     *
     * @param video
     * @param ip
     * @param userAgent
     */
    public void handleNewViewLog(Video video, String ip, String userAgent) {
        String videoId = video.getVideoId();
        int newViewCount = video.getViewCount() + 1;
        //保存新的viewLog记录
        ViewLog viewLog = new ViewLog();
        viewLog.setVideoId(videoId);
        viewLog.setIp(ip);
        viewLog.setUserAgent(userAgent);
        viewLog.setViewTime(new Date());
        viewLog.setNumberOfThisVideo(newViewCount);
        viewLogDao.save(viewLog);
        //更新video
        video.setViewCount(newViewCount);
        videoDao.save(video);
    }
}
