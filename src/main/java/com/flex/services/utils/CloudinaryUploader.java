package com.flex.services.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flex.models.ImageModel;
import com.flex.viewModels.ImageUploadingViewModel;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryUploader implements ImageUploader {
    @Value("${spring.application.cloud_name}")
    private String cloudName;

    @Value("${spring.application.cloudinary_api_key}")
    private String api_key;

    @Value("${spring.application.cloudinary_api_secret}")
    private String api_secret;

    @Value("${spring.application.storage_path}")
    private String storage_path;

    @Override
    public ImageModel upload(ImageUploadingViewModel image) throws IOException {

        String name = String.format("%s/%s", storage_path, RandomStringUtils.randomAlphanumeric(32));
        return createFromResultMap(
                uploadImage(image.getFile().getBytes(), name));
    }

    /**
     * Result map format:
     * https://cloudinary.com/documentation/java_image_and_video_upload#upload_response
     *
     * @param bytes
     * @return uploading result map
     * @throws IOException
     */
    private Map uploadImage(byte[] bytes, String name) throws IOException {

        Map imageUploadConfig = ObjectUtils.asMap(
                "public_id", name,
                "resource_type", "image"
        );
        Cloudinary cloudinary = new Cloudinary(config());
        return cloudinary.uploader().upload(bytes, imageUploadConfig);
    }

    private Map config() {
        return ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", api_key,
                "api_secret", api_secret
        );
    }

    /**
     * Result map format:
     * https://cloudinary.com/documentation/java_image_and_video_upload#upload_response
     *
     * @param map uploading result from cloudinary
     * @return model without: id, user_id
     */
    private ImageModel createFromResultMap(Map map) {
        ImageModel model = new ImageModel();
        model.setWidth((Integer) map.get("width"));
        model.setHeight((Integer) map.get("height"));
        model.setUrl((String) map.get("url"));
        return model;
    }
}
