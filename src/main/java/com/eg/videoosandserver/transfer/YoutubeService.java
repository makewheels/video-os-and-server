package com.eg.videoosandserver.transfer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import com.eg.videoosandserver.util.*;
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
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
        String watchUrl = videoService.getWatchUrl(videoId);
        new Thread(() -> {
            //获取视频文件信息
            String getFilenameCmd = "yt-dlp --get-filename -o '%(title)s.%(ext)s' "
                    + "--restrict-filenames " + youtubeUrl;
            System.out.println("getFilenameCmd = " + getFilenameCmd);
            String filename = RuntimeUtil.execForStr(getFilenameCmd);
            filename = filename.replace("'", "");
            filename = filename.replace("+", "_");
            filename = filename.replace("%", "_");
            filename = filename.replace("#", "_");
            filename = filename.replace("&", "_");
            filename = filename.replace("\"", "_");
            filename = filename.replace("<", "_");
            filename = filename.replace(">", "_");
            filename = filename.replace("~", "_");
            filename = filename.replace("^", "_");
            filename = filename.replace("!", "_");
            filename = filename.replace("@", "_");
            filename = filename.replace("$", "_");
            filename = filename.replace("*", "_");
            filename = filename.replace("`", "_");
            filename = filename.replace("(", "_");
            filename = filename.replace(")", "_");
            filename = filename.replace(" ", "_");
            filename = filename.replace("\n", "");
            filename = filename.replace("\r", "");
            System.out.println("filename = " + filename);

            //下载视频
            File workDir = videoService.getWorkDir();
            File webmFile = new File(workDir, videoId + "/download/" + filename);
            System.out.println("webmFile = " + webmFile);
            String downloadCmd = "yt-dlp -S height:1080 -o " + webmFile.getAbsolutePath() + " " + youtubeUrl;
            System.out.println("downloadCmd = " + downloadCmd);
            executeAndPrint(downloadCmd);

            //上传对象存储
            System.out.println("开始上传对象存储");
            long start = System.currentTimeMillis();

            String base = BaiduCloudUtil.getObjectStoragePrefix(videoId);
            String key = base + videoId + "." + FilenameUtils.getExtension(webmFile.getName());
            System.out.println("key = " + key);
            String playFileUrl = BaiduCloudUtil.uploadObjectStorage(webmFile, key);

            System.out.println("上传对象存储完成");
            System.out.println("playFileUrl = " + playFileUrl);
            long end = System.currentTimeMillis();
            long time = end - start;
            long fileLength = webmFile.length();
            long speedPerSecond = fileLength / time * 1000;
            String readable = FileUtil.readableFileSize(speedPerSecond);
            System.out.println("上传对象存储速度:  " + readable + "/s");

            //通知
            System.out.println("通知国内服务器");
            notifyWebmVideo(videoId, playFileUrl, webmFile);

            //删除本地下载的文件
            System.out.println("FileUtil.del(new File(workDir, videoId)) = "
                    + FileUtil.del(new File(workDir, videoId)));

            //钉钉通知我
            DingDingUtil.sendMarkdown("搬运完成 " + videoId + "\n\n" + filename);
            DingDingUtil.sendMarkdown(watchUrl);
        }).start();

        //提前先返回播放地址
        return watchUrl;
    }

    private String notifyWebmVideo(String videoId, String playFileUrl, File file) {
        String notifyUrl = Constants.BASE_URL + "/notifyNewVideo";
        Map<String, String> map = new HashMap<>();
        map.put("password", Constants.PASSWORD);
        map.put("videoId", videoId);
        map.put("type", Constants.TYPE_WEBM);
        map.put("playFileUrl", playFileUrl);
        map.put("m3u8FileUrl", null);
        map.put("tsAmount", "0");
        map.put("videoFileFullName", file.getName());
        map.put("videoFileBaseName", FilenameUtils.getBaseName(file.getName()));
        map.put("videoFileExtension", FilenameUtils.getExtension(file.getName()));
        String watchUrl = HttpUtil.post(notifyUrl, map);
        System.out.println("watchUrl: " + watchUrl);
        return watchUrl;
    }
}
