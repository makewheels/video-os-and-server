package com.eg.videoosandserver.prepare;

import com.eg.videoosandserver.util.BaiduCloudUtil;
import com.eg.videoosandserver.util.MakeM3u8Result;
import com.eg.videoosandserver.util.RandomUtil;
import com.eg.videoosandserver.util.VideoUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 总的运行流程
 */
public class Run {

    private static MakeM3u8Result transcoding(File videoFile, String videoId) {
        try {
            return VideoUtil.makeM3u8(videoFile, videoId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void uploadToObjectStorage(MakeM3u8Result makeM3u8Result, String videoId) {
        File m3u8File = makeM3u8Result.getM3u8File();
        String base = "/videos/" + videoId + "/";
        String m3u8FileUrl = BaiduCloudUtil.uploadObjectStorage(
                m3u8File, base + m3u8File.getName());
        makeM3u8Result.setM3u8FileUrl(m3u8FileUrl);
        List<File> tsFileList = makeM3u8Result.getTsFileList();
        List<String> tsFileUrlList = new ArrayList<>();
        for (int i = 0; i < tsFileList.size(); i++) {
            File file = tsFileList.get(i);
            String tsFileUrl = BaiduCloudUtil.uploadObjectStorage(file, base + file.getName());
            System.out.println(tsFileUrl);
            tsFileUrlList.add(i, tsFileUrl);
        }
        makeM3u8Result.setTsFileUrlList(tsFileUrlList);
    }

    public static void main(String[] args) {
        String videoFilePath = "C:\\Users\\thedoflin\\Videos\\Desktop\\Desktop 2020.07.05 - 20.33.54.01.mp4";
        File videoFile = new File(videoFilePath);
        String videoId = RandomUtil.getString();
        //转码
        MakeM3u8Result makeM3u8Result = transcoding(videoFile, videoId);
        //上传对象存储
        uploadToObjectStorage(makeM3u8Result, videoId);
        //删除本地转码文件
        System.out.println();
        //通知服务器
    }
}
