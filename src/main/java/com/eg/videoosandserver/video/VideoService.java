package com.eg.videoosandserver.video;

import com.alibaba.fastjson.JSONObject;
import com.eg.videoosandserver.util.Constants;
import com.eg.videoosandserver.util.DingDingUtil;
import com.eg.videoosandserver.util.IpUtil;
import com.eg.videoosandserver.util.UserAgentParser;
import com.eg.videoosandserver.viewlog.ViewLogService;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Date;

@Service
@Transactional
public class VideoService {
    @Value("${workDir}")
    private String workDir;

    @Resource
    private VideoDao videoDao;
    @Resource
    private ViewLogService viewLogService;

    public File getWorkDir() {
        File file = new File(workDir);
        if (!file.exists()) file.mkdirs();
        return file;
    }

    public String getWatchUrl(String videoId) {
        return Constants.BASE_URL + "/watch?v=" + videoId;
    }

    private String newHlsVideo(
            String videoId, String type, String m3u8FileUrl, int tsAmount,
            String videoFileFullName, String videoFileBaseName,
            String videoFileExtension) {
        Video video = new Video();
        video.setViewCount(0);
        video.setCreateTime(new Date());
        video.setVideoId(videoId);
        video.setType(type);
        video.setM3u8_file_url(m3u8FileUrl);
        video.setTsAmount(tsAmount);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        String watchUrl = getWatchUrl(videoId);
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
        video.setType(type);
        video.setPlayFileUrl(playFileUrl);
        video.setVideoFileFullName(videoFileFullName);
        video.setVideoFileBaseName(videoFileBaseName);
        video.setVideoFileExtension(videoFileExtension);
        String watchUrl = getWatchUrl(videoId);
        video.setWatchUrl(watchUrl);
        videoDao.save(video);
        return watchUrl;
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
            return newHlsVideo(videoId, type, m3u8FileUrl, tsAmount, videoFileFullName,
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
    public void watchNotify(Video video, String ip, String location, String userAgentString) {
        String markdownText =
                "# video: " + video.getVideoFileBaseName() + "\n\n" +
                        "# viewCount: " + video.getViewCount() + "\n\n" +
                        "# ip: " + ip + "\n\n" +
                        "# location: " + location + "\n\n" +
                        "# deviceInfo: " + UserAgentParser.getDeviceInfo(userAgentString);
        DingDingUtil.sendMarkdown(markdownText);
    }

    /**
     * 处理观看视频记录
     *
     * @param request
     * @param videoId
     * @return
     */
    public Video handleWatchVideo(HttpServletRequest request, String videoId) {
        //找到这个视频
        Video video = getVideoByVideoId(videoId);
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        //记录以及获取ip，钉钉通知异步完成，先返回给前端页面
        new Thread(() -> {
            //获取ip位置
            JSONObject ipJson = IpUtil.getLocationByIp(ip);
            //解析ip位置json
            JSONObject result = ipJson.getJSONObject("result");
            String province = result.getString("province");
            String city = result.getString("city");
            String district = result.getString("district");
            String location = province + " " + city + " " + district;
            //保存viewLog
            viewLogService.handleNewViewLog(video, ip, ipJson, userAgent);
            //钉钉通知
            watchNotify(video, ip, location, userAgent);
        }).start();
        return video;
    }

}
