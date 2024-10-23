package com.example.onlinenews.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.onlinenews.publisher.service.PublisherService;
import com.example.onlinenews.user.dto.GeneralCreateRequestDTO;
import com.example.onlinenews.user.dto.JournallistCreateRequestDTO;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.repository.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PublisherService publisherService;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String buketName;

    @Autowired
    public UserService(UserRepository userRepository, PublisherService publisherService, PasswordEncoder passwordEncoder, AmazonS3 amazonS3) {
        this.userRepository = userRepository;
        this.publisherService = publisherService;
        this.passwordEncoder = passwordEncoder;
        this.amazonS3 = amazonS3;
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public Optional<User> read(Long id){
        return userRepository.findById(id);
    }

    public Boolean emailExists(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public void createGeneralUser(GeneralCreateRequestDTO requestDTO){
        User user = User.builder()
                .email(requestDTO.getUser_email())
                .pw(requestDTO.getUser_pw())
                .sex(requestDTO.getUser_sex())
                .cp(requestDTO.getUser_cp())
                .name(requestDTO.getUser_name())
                .img(requestDTO.getUser_img())
                .bio(null)
                .publisher(null)
                .createdAt(LocalDateTime.now())
                .grade(UserGrade.GENERAL_MEMBER).build();

        userRepository.save(user);
    }

    public void createJournalistUser(JournallistCreateRequestDTO requestDTO){
        User user = User.builder()
                .email(requestDTO.getUser_email())
                .pw(requestDTO.getUser_pw())
                .sex(requestDTO.getUser_sex())
                .cp(requestDTO.getUser_cp())
                .name(requestDTO.getUser_name())
                .img(requestDTO.getUser_img())
                .bio(null)
                .publisher(publisherService.getPublisherByName(requestDTO.getPublisher()))
                .createdAt(LocalDateTime.now())
                .grade(UserGrade.CITIZEN_REPORTER).build();

        userRepository.save(user);
    }

    public String saveProfileImg(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFilename = UUID.randomUUID() + fileExtension;
        String fileUrl = "https://" + buketName + ".s3.amazonaws.com/profileImg/" + uniqueFilename;

        try {
            amazonS3.putObject(new PutObjectRequest(buketName,uniqueFilename,file.getInputStream(),null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3");
        }

        return fileUrl;
    }
}
