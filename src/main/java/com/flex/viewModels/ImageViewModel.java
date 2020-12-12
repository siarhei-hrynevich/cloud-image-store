package com.flex.viewModels;

import com.flex.models.ImageModel;

import java.util.List;

public class ImageViewModel {

    private static final String commonUrlPath = System.getenv("commonImagesUrlPath");

    private Long id;
    private Long user_ID;
    private String name;
    private String url;
    private Integer width;
    private Integer height;
    private List<String> tags;
    private Long downloads;

    public ImageViewModel() {}

    public ImageViewModel(ImageModel model) {
        id = model.getId();
        user_ID = model.getUserID();
        name = model.getName();
        url = model.getSmallUrl();
        width = model.getWidth();
        height = model.getHeight();
        tags = model.getTags();
        downloads = model.getDownloads();
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ImageModel toModel() {
        ImageModel model = new ImageModel();
        model.setSmallUrl(url);
        model.setTags(tags);
        model.setHeight(height);
        model.setWidth(width);
        model.setId(id);
        model.setUserID(user_ID);
        model.setName(name);
        model.setDownloads(downloads);
        return model;
    }

    public Long getDownloads() {
        return downloads;
    }

    public void setDownloads(Long downloads) {
        this.downloads = downloads;
    }
}