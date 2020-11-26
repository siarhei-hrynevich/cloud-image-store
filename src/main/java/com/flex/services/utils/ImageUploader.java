package com.flex.services.utils;

import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ImageModel;
import com.flex.viewModels.ImageUploadingViewModel;

import java.io.IOException;

public interface ImageUploader {
    ImageModel upload(ImageUploadingViewModel image) throws IOException;

    void deleteImage(ImageModel model) throws IOException, ImageNotFoundException;
}
