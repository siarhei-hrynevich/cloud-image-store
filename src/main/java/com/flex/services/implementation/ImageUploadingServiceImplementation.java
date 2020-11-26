package com.flex.services.implementation;

import com.flex.dao.ImageDao;
import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.services.ImageUploadingService;
import com.flex.services.utils.ImageUploader;
import com.flex.viewModels.ImageUploadingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageUploadingServiceImplementation implements ImageUploadingService {

    @Autowired
    ImageDao imageDao;

    @Autowired
    ImageUploader uploader;

    @Override
    public ImageModel uploadImage(ImageUploadingViewModel image, ExtendedUserDetails user) throws IOException {
        ImageModel model = uploader.upload(image);
        model.setName(image.getName());
        model.setUserID(user.getId());
        model = imageDao.create(model);
        model.makeExtendedUrl();
        return model;
    }

    @Override
    public void deleteImage(ImageModel model) throws IOException, ImageNotFoundException {
        uploader.deleteImage(model);
        imageDao.deleteImage(model);
    }

}
