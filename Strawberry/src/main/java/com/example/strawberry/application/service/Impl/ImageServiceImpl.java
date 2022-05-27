//package com.example.strawberry.application.service.Impl;
//
//import com.example.strawberry.application.dai.IImageRepository;
//import com.example.strawberry.application.dai.IPostRepository;
//import com.example.strawberry.application.service.IImageService;
//import com.example.strawberry.application.utils.UploadFile;
//import com.example.strawberry.domain.dto.ImageDTO;
//import com.example.strawberry.domain.entity.Image;
//import com.example.strawberry.domain.entity.Post;
//import com.example.strawberry.domain.entity.User;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Optional;
//import java.util.Set;
//
//@Service
//public class ImageServiceImpl implements IImageService {
//
//    @Autowired
//    private IPostRepository postRepository;
//
//    @Autowired
//    private IImageRepository imageRepository;
//
//    @Autowired
//    private PostServiceImpl postService;
//
//    @Autowired
//    private UploadFile uploadFile;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Override
//    public Image upImage(Long idPost, MultipartFile fileImage) throws IOException{
//        Optional<Post> post = postRepository.findById(idPost);
//        postService.checkPostExists(post);
//        Image image = new Image();
//        image.setLinkImage(uploadFile.getUrlFromFile(fileImage));
//        image.setPost(post.get());
//        imageRepository.save(image);
//        return image;
//    }
//
////    @Override
////    public Set<Image> getAllImageByIdPost(Long idPost) {
////        Set<Image> images = postRepository.findById(idPost).get().getImages();
////        return images;
////    }
//
//}
