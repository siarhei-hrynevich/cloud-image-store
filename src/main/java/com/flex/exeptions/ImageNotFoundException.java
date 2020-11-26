package com.flex.exeptions;

public class ImageNotFoundException extends Exception {
    private String imageName;
    public ImageNotFoundException(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
