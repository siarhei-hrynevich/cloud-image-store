package com.flex.models;


public class ImageModel {

    private static final String commonUrlPath = System.getenv("commonImagesUrlPath");

    private Long id;
    private Long user_ID;
    private String name;
    private String url;
    private Integer width;
    private Integer height;

    public ImageModel() {
    }

    public ImageModel(Long id, Long userID, String name, String url, Integer width, Integer height) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.width = width;
        this.height = height;
        this.user_ID = userID;
    }

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
    }

    public void makeExtendedUrl() {
        if(!url.contains(commonUrlPath)) {
            url = commonUrlPath + url;
        }
    }

}