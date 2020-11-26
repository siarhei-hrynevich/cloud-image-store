package com.flex.viewModels;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadingViewModel {
    private String name;
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
}
