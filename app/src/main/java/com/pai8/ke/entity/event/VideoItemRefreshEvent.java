package com.pai8.ke.entity.event;

import com.pai8.ke.entity.Video;

public class VideoItemRefreshEvent {
    private int position;
    private Video video;

    public VideoItemRefreshEvent(int position, Video video) {
        this.position = position;
        this.video = video;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
