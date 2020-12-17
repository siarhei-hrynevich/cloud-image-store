package com.flex.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flex.dao.ImageDao;
import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.services.ImageUploadingService;
import com.flex.viewModels.ImageUploadingViewModel;
import com.flex.viewModels.ImageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImagesRESTController {

    private final ImageDao dao;

    @Autowired
    private ImageUploadingService service;

    @Autowired
    public ImagesRESTController(ImageDao dao) {
        this.dao = dao;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ImageViewModel>> search(String name) {
        List<ImageViewModel> images = dao.findByName(name);
        images.forEach(ImageViewModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/tag_search")
    public ResponseEntity<List<ImageViewModel>> searchImagesWithTag(String name, String tag) {
        List<ImageViewModel> images = dao.findByNameWithTag(name, tag);
        images.forEach(ImageViewModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ImageViewModel>> findByUserId(@PathVariable Long id) {
        List<ImageViewModel> models = dao.findByUserId(id);
        models.stream().forEach(ImageViewModel::makeExtendedUrl);
        return ResponseEntity.ok(models);
    }

    @PostMapping("/")
    @Secured("ROLE_ADMIN,ROLE_USER")
    public ResponseEntity<ImageViewModel> uploadImage(ImageUploadingViewModel image, HttpServletRequest request) {
        try {
            ImageViewModel model = service.uploadImage(image, BaseController.getCurrentUser());
            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/last")
    public ResponseEntity<List<ImageViewModel>> lastUploadedImages(int count) {
        if (count > 0) {
            List<ImageViewModel> images = dao.getLastImages(count);
            images.forEach(ImageViewModel::makeExtendedUrl);
            return ResponseEntity.ok().body(images);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageViewModel> findById(@PathVariable Long id) {
        ImageViewModel model = new ImageViewModel(dao.findById(id));
        model.makeExtendedUrl();
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN,ROLE_USER")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        ImageViewModel model = new ImageViewModel(dao.findById(id));
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        ExtendedUserDetails user = BaseController.getCurrentUser();
        assert user != null;
        if (model.getUserID().equals(user.getId()) || BaseController.hasRole(user, "ROLE_ADMIN")) {
            return deleteValidImage(model);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity deleteValidImage(ImageViewModel model) {
        try {
            service.deleteImage(model);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<ImageViewModel>> findByCurrentUserId() {
        ExtendedUserDetails user = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<ImageViewModel> images = dao.findByUserId(user.getId());
        images.stream().forEach(ImageViewModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/totalImagesCount")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(dao.count());

    }

    @GetMapping("/download-link/{id}")
    public ResponseEntity<String> getDownloadLink(@PathVariable Long id) {
        ImageModel model = dao.findById(id);
        model.makeExtendedUrl();
        dao.incrementDownloadsOfImage(id);
        return ResponseEntity.ok(model.getUrl());
    }

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getAllTags() {
        return ResponseEntity.ok(dao.getAllTags());
    }

}
