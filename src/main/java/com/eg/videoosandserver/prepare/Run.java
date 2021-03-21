package com.eg.videoosandserver.prepare;

import com.eg.videoosandserver.util.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 总的运行流程
 */
public class Run {
    /**
     * 视频转码
     *
     * @param videoFile
     * @param videoId
     * @return
     */
    private static MakeM3u8Result transcodingVideo(File videoFile, String videoId) {
        try {
            return VideoUtil.makeM3u8(videoFile, videoId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传ts碎片到对象存储
     *
     * @param makeM3u8Result
     * @param videoId
     */
    private static void uploadToObjectStorage(MakeM3u8Result makeM3u8Result, String videoId) {
        //准备对象存储前缀
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthString = month + "";
        if (month <= 9) {
            monthString = "0" + month;
        }
        String base = "/videos/" + year + "-" + monthString + "/" + videoId + "/";
        //上传m3u8文件
        File m3u8File = makeM3u8Result.getM3u8File();
        String m3u8FileUrl = BaiduCloudUtil.uploadObjectStorage(
                m3u8File, base + m3u8File.getName());
        makeM3u8Result.setM3u8FileUrl(m3u8FileUrl);
        //上传ts碎片
        List<File> tsFileList = makeM3u8Result.getTsFileList();
        List<String> tsFileUrlList = new ArrayList<>();
        int total = tsFileList.size();
        for (int i = 0; i < total; i++) {
            File file = tsFileList.get(i);
            String tsFileUrl = BaiduCloudUtil.uploadObjectStorage(
                    file, base + file.getName());
            int current = i + 1;
            int progress = (int) (current * 1.0 / total * 100);
            System.out.println(progress + "% " + tsFileUrl);
            tsFileUrlList.add(i, tsFileUrl);
        }
        makeM3u8Result.setTsFileUrlList(tsFileUrlList);
    }

    /**
     * 准备文件名
     *
     * @param makeM3u8Result
     * @param videoFile
     */
    private static void prepareFileName(MakeM3u8Result makeM3u8Result, File videoFile) {
        makeM3u8Result.setVideoFileFullName(videoFile.getName());
        makeM3u8Result.setVideoFileBaseName(FilenameUtils.getBaseName(videoFile.getName()));
        makeM3u8Result.setVideoFileExtension(FilenameUtils.getExtension(videoFile.getName()));
    }

    /**
     * 通知我的服务器新增视频
     *
     * @param makeM3u8Result
     */
    private static void notifyNewVideo(MakeM3u8Result makeM3u8Result) {
//        String notifyUrl
//        = "https://" + Contants.IP + ":5002/video-os-and-server/video/notifyNewVideo";
        String notifyUrl = "https://" + Contants.IP + "/notifyNewVideo";
        Map<String, String> map = new HashMap<>();
        map.put("password", "N9Q0HsaSniSNiQ94");
        map.put("videoId", makeM3u8Result.getId());
        map.put("m3u8FileUrl", makeM3u8Result.getM3u8FileUrl());
        map.put("tsAmount", makeM3u8Result.getTsFileList().size() + "");
        map.put("videoFileFullName", makeM3u8Result.getVideoFileFullName());
        map.put("videoFileBaseName", makeM3u8Result.getVideoFileBaseName());
        map.put("videoFileExtension", makeM3u8Result.getVideoFileExtension());
        String watchUrl = HttpUtil.post(notifyUrl, map);
        System.out.println("watchUrl: " + watchUrl);
    }

    public static void main(String[] args) {
        //是否转码为720p
        boolean transcodeTo720p = false;
        String videoFilePath
                = "C:\\Users\\thedoflin\\Documents\\Tencent Files\\1444700314\\FileRecv\\Screenrecorder-2021-03-21-12-33-38-776.mp4";
        //原始视频
        File originalVideoFile = new File(videoFilePath);
        //最终要转m3u8上传的视频
        File finalVideoFile = originalVideoFile;
        //转码720p的视频
        File transcode720pVideoFile = null;
        if (transcodeTo720p) {
            transcode720pVideoFile = VideoUtil.transcodeTo720p(originalVideoFile);
            finalVideoFile = transcode720pVideoFile;
        }
        String videoId = RandomUtil.getString();
        //转码
        MakeM3u8Result makeM3u8Result = transcodingVideo(finalVideoFile, videoId);
        //准备名字
        prepareFileName(makeM3u8Result, finalVideoFile);
        //上传对象存储
        uploadToObjectStorage(makeM3u8Result, videoId);
        System.out.println("upload finished!");
        //删除本地转码文件
        VideoUtil.deleteTranscodeFiles(makeM3u8Result);
        //删除720p转码视频
        if (transcodeTo720p) {
            transcode720pVideoFile.delete();
        }
        //通知服务器
        notifyNewVideo(makeM3u8Result);
        //结束
        System.exit(0);
    }

}
