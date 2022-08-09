package com.example.strawberry.application.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.strawberry.config.exception.UploadImageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class UploadFile {

    @Autowired
    private Cloudinary cloudinary;

    public String getUrlFromFile(MultipartFile multipartFile) {
        try {
            File uploadedFile = convertMultipartToFile(multipartFile);
            Map<?, ?> map = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            return map.get("secure_url").toString();
        } catch (IOException e) {
            throw new UploadImageException("Upload image failed");
        }
    }

    public String getUrlFromLargeFile(MultipartFile multipartFile) throws IOException {
        try {
            File uploadedFile = convertMultipartToFile(multipartFile);
            Map<?, ?> map = cloudinary.uploader().uploadLarge(uploadedFile, ObjectUtils.asMap("resource_type", "video"));
            return map.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeImageFromUrl(String url) {
        try {
            cloudinary.uploader().destroy(url, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new UploadImageException("Upload image failed");
        }
    }

    public static File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
