package com.flex.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flex.dao.ImageDao;
import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.services.ImageUploadingService;
import com.flex.viewModels.ImageUploadingViewModel;
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

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        ExtendedUserDetails user = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return ResponseEntity.ok("User id: " + user.getId());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ImageModel>> search(String name) {
        List<ImageModel> images = dao.findByName(name);
        images.forEach(ImageModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

    @PostMapping("/")
    @Secured("ROLE_CREATOR,ROLE_ADMIN,ROLE_USER")
    public ResponseEntity<ImageModel> uploadImage(ImageUploadingViewModel image, HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        Object user = SecurityContextHolder.getContext().getAuthentication().getDetails();
        try {
            ImageModel model = service.uploadImage(image, (ExtendedUserDetails) user);
            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/last")
    public ResponseEntity<List<ImageModel>> lastUploadedImages(int count) {
        if (count > 0) {
            List<ImageModel> images = dao.getLastImages(count);
            images.forEach(ImageModel::makeExtendedUrl);
            return ResponseEntity.ok().body(images);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageModel> findById(@PathVariable Long id) {
        ImageModel model = dao.findById(id);
        model.makeExtendedUrl();
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteImage(@PathVariable Long id) {
        ImageModel model = dao.findById(id);
        ExtendedUserDetails user = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (model == null)
            return ResponseEntity.notFound().build();
        if (!model.getUserID().equals(user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return deleteValidImage(model);
    }

    private ResponseEntity deleteValidImage(ImageModel model) {
        try {
            service.deleteImage(model);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ImageModel>> findByUserId(@PathVariable Long id) {
        List<ImageModel> models = dao.findByUserId(id);
        models.stream().forEach(ImageModel::makeExtendedUrl);
        return ResponseEntity.ok(models);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ImageModel>> findByCurrentUserId() {
        ExtendedUserDetails user = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<ImageModel> images = dao.findByUserId(user.getId());
        images.stream().forEach(ImageModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

}
