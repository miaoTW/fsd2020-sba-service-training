package com.fsd.sba.domain;

import java.io.Serializable;

/**
 * A Technologies.
 */

public class Technologies implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String toc;
    private String duration;
    private String prerequites;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToc() {
        return toc;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrerequites() {
        return prerequites;
    }

    public void setPrerequites(String prerequites) {
        this.prerequites = prerequites;
    }
}
