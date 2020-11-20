package com.pai8.ke.entity;

import android.graphics.Paint;

import java.io.Serializable;

public class PushBiz implements Serializable {

    private String m_type;
    private String content;

    public String getM_type() {
        return m_type;
    }

    public void setM_type(String m_type) {
        this.m_type = m_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
