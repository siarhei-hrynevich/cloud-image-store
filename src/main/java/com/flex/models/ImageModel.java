package com.flex.models;

import java.util.List;

public class ImageModel {

    private static final String commonUrlPath = System.getenv("commonImagesUrlPath");

    private Long id;
    private Long user_ID;
    private String name;
    private String url;
    private String smallUrl;
    private Integer width;
    private Integer height;
    private List<String> tags;
    private Long downloads;

    public ImageModel() { url = ""; }

    public String getUrl() {
        return url;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public Long getUserID() {
        return user_ID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserID(Long id) {
        user_ID = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void makeShortUrl() {
        url = url.replace(commonUrlPath, "");
        smallUrl = smallUrl.replace(commonUrlPath, "");
    }

    public void makeExtendedUrl() {
        if(!url.contains(commonUrlPath)) {
            url = commonUrlPath + url;
            smallUrl = commonUrlPath + smallUrl;
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }
}