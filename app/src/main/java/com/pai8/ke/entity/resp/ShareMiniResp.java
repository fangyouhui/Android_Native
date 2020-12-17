package com.pai8.ke.entity.resp;

public class ShareMiniResp {

    /**
     * miniprogramType : 1
     * app_id : wx9fe0857d70e70bf7
     * app_path : https://video.5pai8.com/video?action=gogogo&id=100
     * webpage_url :
     * title : 开车需谨慎
     * description : 开车需谨慎
     * thumb : https://jianshen.fyh5p8.com/o_1ehmbqp1g1c9ho651o64o821uo6a.mp4?vframe/jpg/offset/0
     */

    private int miniprogramType;
    private String app_id;
    private String app_path;
    private String webpage_url;
    private String title;
    private String description;
    private String thumb;

    public int getMiniprogramType() {
        return miniprogramType;
    }

    public void setMiniprogramType(int miniprogramType) {
        this.miniprogramType = miniprogramType;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getApp_path() {
        return app_path;
    }

    public void setApp_path(String app_path) {
        this.app_path = app_path;
    }

    public String getWebpage_url() {
        return webpage_url;
    }

    public void setWebpage_url(String webpage_url) {
        this.webpage_url = webpage_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
