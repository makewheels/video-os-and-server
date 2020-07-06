package com.eg.videoosandserver.util;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-03 19:26
 */
@Data
public class MakeM3u8Result {
    private String videoFileFullName;
    private String videoFileBaseName;
    private String videoFileExtension;

    private String id;
    private File folder;
    private File m3u8File;
    private String m3u8FileUrl;
    private List<File> tsFileList;
    private List<String> tsFileUrlList = new ArrayList<>();
}
