package com.eg.videoosandserver.util;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频工具类
 *
 * @time 2020-02-03 18:59
 */
public class VideoUtil {

    /**
     * 转码为720p
     *
     * @param originalVideoFile
     * @return
     */
    public static File transcodeTo720p(File originalVideoFile) {
        String newName = FilenameUtils.getBaseName(originalVideoFile.getName()) + "_720p."
                + FilenameUtils.getExtension(originalVideoFile.getName());
        File resultFile = new File(originalVideoFile.getParent(), newName);
        String cmd = "ffmpeg -i \"" + originalVideoFile.getAbsolutePath()
                + "\" -c copy -c:v libx264 -vf scale=-2:720 \"" + resultFile.getAbsolutePath();
        System.out.println(cmd);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getErrorStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultFile;
    }

    /**
     * 转码制作m3u8
     *
     * @param videoFile
     * @param videoId
     * @return
     * @throws IOException
     */
    public static MakeM3u8Result makeM3u8(File videoFile, String videoId) throws IOException {
        //创建目录
        File folder = new File(videoFile.getParent(), videoId);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File m3u8File = new File(folder.getAbsolutePath() + File.separator +
                videoId + ".m3u8");
        //转码视频
        String cmd = "ffmpeg -i \"" + videoFile.getAbsolutePath()
                + "\" -codec copy -vbsf h264_mp4toannexb -map 0 -f segment -segment_list \""
                + m3u8File.getAbsolutePath() + "\" -segment_time 1 \"" + folder.getAbsolutePath()
                + File.separator + videoId + "-%d.ts\"";
        System.out.println(cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        //设置返回的结果
        MakeM3u8Result makeM3u8Result = new MakeM3u8Result();
        makeM3u8Result.setId(videoId);
        makeM3u8Result.setFolder(folder);
        makeM3u8Result.setM3u8File(m3u8File);
        //把所有ts文件都加入集合
        List<File> tsFileList = new ArrayList<>();
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".ts")) {
                tsFileList.add(file);
            }
        }
        makeM3u8Result.setTsFileList(tsFileList);
        return makeM3u8Result;
    }

    public static void deleteTranscodeFiles(MakeM3u8Result makeM3u8Result) {
        //删除m3u8
        makeM3u8Result.getM3u8File().delete();
        //删除ts碎片
        List<File> tsFileList = makeM3u8Result.getTsFileList();
        for (File file : tsFileList) {
            file.delete();
        }
        //删除文件夹
        makeM3u8Result.getFolder().delete();
    }

}
