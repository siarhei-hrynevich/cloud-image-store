package com.flex.viewModels;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageUploadingViewModel {
    private String name;
    private List<String> tags;
    private MultipartFile file;

    public ImageUploadingViewModel(MultipartFile file, String name) {
        this.file = file;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
