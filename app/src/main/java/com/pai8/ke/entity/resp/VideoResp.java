package com.pai8.ke.entity.resp;

import com.pai8.ke.entity.Video;

import java.io.Serializable;

/**
 * 视频Entity
 */
public class VideoResp implements Serializable {

    private Video video;

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
