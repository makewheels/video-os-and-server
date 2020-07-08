package com.eg.videoosandserver.viewlog;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 观看记录
 *
 * @time 2020-03-28 19:00
 */
@Data
@Entity
public class ViewLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //看这个视频的第几号
    private int numberOfThisVideo;
    private String videoId;
    private Date viewTime;
    private String userAgent;
    private String ip;
}
