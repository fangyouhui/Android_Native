package com.pai8.ke.entity.resp;

import java.util.List;

public class City {

    private int id;
    private String name;
    private int pid;
    private List<District> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<District> getChildren() {
        return children;
    }

    public void setChildren(List<District> children) {
        this.children = children;
    }
}
