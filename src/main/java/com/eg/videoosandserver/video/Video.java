package com.eg.videoosandserver.video;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 视频类
 */
@Data
@Entity
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date createTime;

    private String videoId;
    private String videoFileFullName;
    private String videoFileBaseName;
    private String videoFileExtension;
    private String m3u8FileUrl;
    private int tsAmount;
}
