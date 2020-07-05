package com.eg.videoosandserver.test;

import com.eg.videoosandserver.util.MakeM3u8Result;
import com.eg.videoosandserver.util.VideoUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {
        File videoFile = new File("C:\\Users\\thedoflin\\Videos\\Desktop\\Desktop 2020.07.05 - 20.33.54.01.mp4");
        MakeM3u8Result m3u8Result = VideoUtil.makeM3u8(videoFile, "ef");
        File m3u8File = m3u8Result.getM3u8File();
        System.out.println(m3u8File.getPath());
        System.out.println(
        );
        List<File> tsFileList = m3u8Result.getTsFileList();
        for (File file : tsFileList) {
            System.out.println(file.getPath());
        }
    }
}
