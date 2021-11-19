package com.eg.videoosandserver.transfer;

import cn.hutool.core.util.RuntimeUtil;
import com.eg.videoosandserver.util.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class YoutubeService {
    /**
     * 提交新视频任务
     */
    public String submitNewVideoMission(String youtubeUrl) {
        //获取视频文件信息
        String filename = RuntimeUtil.execForStr("yt-dlp --get-filename -o '%(title)s.%(ext)s' "
                + "--restrict-filenames" + youtubeUrl);
        File workDir = VideoUtil.getWorkDir();
        String videoId = RandomUtil.getVideoId();
        //下载视频
        File webmFile = new File(workDir, videoId + "/download/" + filename);
        String downloadCmd = "yt-dlp -S 'height:1080' -o '" + filename + "'" + youtubeUrl;

        //上传对象存储
        String base = BaiduCloudUtil.getObjectStoragePrefix(videoId);
        BaiduCloudUtil.uploadObjectStorage(webmFile, base + videoId
                + FilenameUtils.getExtension(webmFile.getName()));
        //通知
        notifyWebmVideo(videoId, webmFile);
        return null;
    }

    private void notifyWebmVideo(String videoId, File file) {
        String notifyUrl = Constants.BASE_URL + "/notifyNewVideo";
        Map<String, String> map = new HashMap<>();
        map.put("password", Constants.PASSWORD);
        map.put("videoId", videoId);
        map.put("type", Constants.TYPE_WEBM);
        map.put("playFileUrl", null);
        map.put("m3u8FileUrl", null);
        map.put("tsAmount", null);
        map.put("videoFileFullName", file.getName());
        map.put("videoFileBaseName", FilenameUtils.getBaseName(file.getName()));
        map.put("videoFileExtension", FilenameUtils.getExtension(file.getName()));
        String watchUrl = HttpUtil.post(notifyUrl, map);
        System.out.println("watchUrl: " + watchUrl);
    }
}
