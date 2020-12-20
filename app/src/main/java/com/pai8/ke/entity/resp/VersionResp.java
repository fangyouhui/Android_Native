package com.pai8.ke.entity.resp;

public class VersionResp {

    /**
     * current_version : 0.9.0
     * upgrade : 0
     * upgrade_url : https://itunes.apple.com/cn/app/123
     * content :
     */

    private String current_version;
    private int upgrade;
    private String upgrade_url;
    private String content;

    public String getCurrent_version() {
        return current_version;
    }

    public void setCurrent_version(String current_version) {
        this.current_version = current_version;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }

    public String getUpgrade_url() {
        return upgrade_url;
    }

    public void setUpgrade_url(String upgrade_url) {
        this.upgrade_url = upgrade_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
