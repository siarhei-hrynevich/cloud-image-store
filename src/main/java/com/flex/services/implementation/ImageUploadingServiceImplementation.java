package com.flex.services.implementation;

import com.flex.dao.ImageDao;
import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.services.ImageUploadingService;
import com.flex.services.utils.ImageUploader;
import com.flex.viewModels.ImageUploadingViewModel;
import com.flex.viewModels.ImageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class ImageUploadingServiceImplementation implements ImageUploadingService {

    @Autowired
    ImageDao imageDao;

    @Autowired
    ImageUploader uploader;

    @Override
    public ImageViewModel uploadImage(ImageUploadingViewModel image, ExtendedUserDetails user) throws IOException {
        ImageModel model = uploader.upload(image);
        model.setName(image.getName());
        model.setUserID(user.getId());
        model.setTags(image.getTags());
        model.getTags().stream().filter(item -> !item.equals(""));
        try {
            model = imageDao.create(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.makeExtendedUrl();
        return new ImageViewModel(model);
    }

    @Override
    public void deleteImage(ImageViewModel model) throws IOException, ImageNotFoundException {
        ImageModel image = model.toModel();
        uploader.deleteImage(image);
        try {
            imageDao.deleteImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
