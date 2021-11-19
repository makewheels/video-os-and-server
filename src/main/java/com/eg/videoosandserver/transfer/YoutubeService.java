package com.eg.videoosandserver.transfer;

import cn.hutool.core.util.RuntimeUtil;
import com.eg.videoosandserver.util.BaiduCloudUtil;
import com.eg.videoosandserver.util.Constants;
import com.eg.videoosandserver.util.HttpUtil;
import com.eg.videoosandserver.util.RandomUtil;
import com.eg.videoosandserver.video.VideoService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class YoutubeService {
    @Resource
    private VideoService videoService;

    public void executeAndPrint(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream errorStream = process.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交新视频任务
     */
    public String submitNewVideoMission(String youtubeUrl) {
        String videoId = RandomUtil.getVideoId();
        //获取视频文件信息
        String filename = RuntimeUtil.execForStr("yt-dlp --get-filename -o '%(title)s.%(ext)s' "
                + "--restrict-filenames" + youtubeUrl);
        File workDir = videoService.getWorkDir();
        //下载视频
        File webmFile = new File(workDir, videoId + "/download/" + filename);
        String downloadCmd = "yt-dlp -S 'height:1080' -o '" + filename + "' " + youtubeUrl;
        executeAndPrint(downloadCmd);

        //上传对象存储
        String base = BaiduCloudUtil.getObjectStoragePrefix(videoId);
        BaiduCloudUtil.uploadObjectStorage(webmFile, base + videoId
                + FilenameUtils.getExtension(webmFile.getName()));
        //通知
        String watchUrl = notifyWebmVideo(videoId, webmFile);
        return watchUrl;
    }

    private String notifyWebmVideo(String videoId, File file) {
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
        return watchUrl;
    }
}
