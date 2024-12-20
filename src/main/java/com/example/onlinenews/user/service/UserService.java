package com.example.onlinenews.user.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.jwt.dto.JwtToken;
import com.example.onlinenews.jwt.provider.JwtTokenProvider;
import com.example.onlinenews.mailing.repository.MailingRepository;
import com.example.onlinenews.publisher.entity.Publisher;
import com.example.onlinenews.publisher.service.PublisherService;
import com.example.onlinenews.request.service.RequestService;
import com.example.onlinenews.user.dto.FindIdRequestDTO;
import com.example.onlinenews.user.dto.FindIdResponseDTO;
import com.example.onlinenews.user.dto.FindPwRequestDTO;
import com.example.onlinenews.user.dto.FindPwResponseDTO;
import com.example.onlinenews.user.dto.GeneralCreateRequestDTO;
import com.example.onlinenews.user.dto.JournallistCreateRequestDTO;
import com.example.onlinenews.user.dto.LoginRequestDTO;
import com.example.onlinenews.user.dto.MypageEditRequestDTO;
import com.example.onlinenews.user.dto.MypageEditResponseDTO;
import com.example.onlinenews.user.dto.MypageResponseDTO;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final MailingRepository mailingRepository;
    private final PublisherService publisherService;
    private final RequestService requestService;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${secretKey.admin}")
    private String adminSecretKey;

    @Value("${secretKey.editor}")
    private String editorSecretKey;

    @Autowired
    public UserService(UserRepository userRepository, MailingRepository mailingRepository,
                       PublisherService publisherService, RequestService requestService,
                       PasswordEncoder passwordEncoder, AmazonS3 amazonS3,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.mailingRepository = mailingRepository;
        this.publisherService = publisherService;
        this.requestService = requestService;
        this.passwordEncoder = passwordEncoder;
        this.amazonS3 = amazonS3;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> read(Long id) {
        return userRepository.findById(id);
    }

    public Boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Long createGeneralUser(GeneralCreateRequestDTO requestDTO) {
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
                .nickname(requestDTO.getUser_nickname())
                .grade(UserGrade.GENERAL_MEMBER).build();

        userRepository.save(user);
        return user.getId();
    }

    public void createJournalistUser(JournallistCreateRequestDTO requestDTO) {
        Publisher publisher = publisherService.getPublisherByName(requestDTO.getPublisher());

        if (publisher == null) {
            throw new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND);
        }

        User user = User.builder()
                .email(requestDTO.getUser_email())
                .pw(requestDTO.getUser_pw())
                .sex(requestDTO.getUser_sex())
                .cp(requestDTO.getUser_cp())
                .name(requestDTO.getUser_name())
                .img(requestDTO.getUser_img())
                .bio(null)
                .createdAt(LocalDateTime.now())
                .grade(UserGrade.CITIZEN_REPORTER).build();

        userRepository.save(user);

        try {
            //시민 기자 등록 요청
            requestService.createEnrollRequest(user, publisher);
        } catch (Exception e) {
            // 오류 발생 시 유저 정보 삭제
            userRepository.delete(user);
            throw e;
        }

    }

    public String saveProfileImg(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFilename = UUID.randomUUID() + fileExtension;
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/profileImg/" + uniqueFilename;

        try {
            amazonS3.putObject(
                    new PutObjectRequest(bucketName, "profileImg/" + uniqueFilename, file.getInputStream(), null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3");
        }

        return fileUrl;
    }

    public void deletePreviousProfileImg(String filePath) {
        String fileKey = filePath.substring(filePath.indexOf("profileImg/"));

        try {
            if (amazonS3.doesObjectExist(bucketName, fileKey)) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        } catch (AmazonServiceException e) {
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    public JwtToken login(LoginRequestDTO requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPw())) {
            throw new BusinessException(ExceptionCode.USER_PASSWORD_INCORRECT);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        System.out.println("login - token 만들기 : " + accessToken + ", " + refreshToken);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build()
                ;

    }

    public boolean checkSecreteKey(UserGrade usergrade, String inviteCode) {
        if (usergrade.equals(UserGrade.SYSTEM_ADMIN)) {
            return inviteCode.equals(adminSecretKey);
        } else if (usergrade.equals(UserGrade.EDITOR)) {
            return inviteCode.equals(editorSecretKey);
        }
        return false;
    }

    public void createAdminUser(String id, String password, UserGrade userGrade, String publisherName) {
        Publisher publisher = null;
        if (!publisherName.isEmpty() || !publisherName.isBlank()) {
            publisher = publisherService.getPublisherByName(publisherName);

            if (publisher == null) {
                throw new BusinessException(ExceptionCode.PUBLISHER_NOT_FOUND);
            }
        }

        String setString = "ADMIN";
        if (userGrade.equals(UserGrade.EDITOR)) {
            setString = "EDITOR";
        }

        User user = User.builder()
                .email(id)
                .pw(passwordEncoder.encode(password))
                .grade(userGrade)
                .sex(true) // 관리자의 나머지 계정 정보의 나머지값들에 "ADMIN" 이 들어가도록 설정
                .cp(setString)
                .name(setString)
                .nickname(setString)
                .publisher(publisher)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public List<FindIdResponseDTO> getEmailWithUsernameAndCp(FindIdRequestDTO requestDTO) {
        String name = requestDTO.getName();
        String cp = requestDTO.getCp();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM");

        List<User> users = userRepository.findAllByNameAndCp(name, cp);

        if (users.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        List<FindIdResponseDTO> responseDTOList = new ArrayList<>();

        for (User user : users) {
            String email = user.getEmail();
            String maskedEmail = maskingEmail(email);

            FindIdResponseDTO respDTO = new FindIdResponseDTO();
            respDTO.setEmail(maskedEmail);

            String formattedCreatedAt = user.getCreatedAt().toString(formatter);
            respDTO.setCreateAt(formattedCreatedAt);

            responseDTOList.add(respDTO);
        }

        return responseDTOList;
    }

    public String maskingEmail(String email) {
        int atIndex = email.indexOf("@");

        if (atIndex <= 5) {
            // 이메일 길이가 짧아 마스킹이 어려운 경우 그대로 반환
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        String maskedUsername = username.substring(0, 2) + "****" + username.substring(username.length() - 2);
        return maskedUsername + domain;
    }

    public FindPwResponseDTO generateTemporaryPassword(FindPwRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String name = requestDTO.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        if (!user.getName().equals(name)) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        String randomPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(randomPassword);

        user.updatePassword(encodedPassword);
        userRepository.save(user);

        FindPwResponseDTO responseDTO = new FindPwResponseDTO();
        responseDTO.setTemporaryPw(randomPassword);

        return responseDTO;
    }

    private String generateRandomPassword() {
        return RandomStringUtils.random(12, true, true) + RandomStringUtils.random(3, true, true);
    }

    public MypageResponseDTO getMypageInfo(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        MypageResponseDTO responseDTO = new MypageResponseDTO();
        responseDTO.setImg(user.getImg());
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setName(user.getName());
        responseDTO.setCp(user.getCp());
        responseDTO.setNickname(user.getNickname());
        responseDTO.setType(String.valueOf(user.getGrade()));
        responseDTO.setSex(user.getSex());
        responseDTO.setBio(user.getBio());
        if (user.getPublisher() != null) {
            responseDTO.setPublisher(user.getPublisher().getName());
        }

        String maskedPassword = "*".repeat(12);
        responseDTO.setEncodedPw(maskedPassword);

        responseDTO.setMailing(mailingRepository.existsByUser(user));
        return responseDTO;
    }

    public MypageEditResponseDTO updateUserInfo(MypageEditRequestDTO requestDTO, String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();

        MypageEditResponseDTO responseDTO = new MypageEditResponseDTO();

        updateField(requestDTO.getNickname(), user::updateNickname, user.getNickname(), responseDTO::setNickname);
        if (requestDTO.getPw() != null) {
            String pw = passwordEncoder.encode(requestDTO.getPw());
            user.updatePassword(pw);
        }

        if (requestDTO.getImg() != null) {  // 새로운 이미지가 존재하는 경우만 처리
            if (user.getImg() != null) {
                deletePreviousProfileImg(user.getImg()); // 기존 이미지 삭제
            }

            String newProfileImgUrl = saveProfileImg(requestDTO.getImg()); // 새 이미지 저장
            updateField(newProfileImgUrl, user::updateImg, user.getImg(), responseDTO::setImg);
        } else {
            responseDTO.setImg(user.getImg()); // 이미지가 변경되지 않은 경우 현재 이미지 유지
        }

        responseDTO.setPw("*".repeat(12));
        updateField(requestDTO.getBio(), user::updateBio, user.getBio(), responseDTO::setBio);
        updateField(requestDTO.getCp(), user::updateCp, user.getCp(), responseDTO::setCp);

        userRepository.save(user);
        return responseDTO;
    }

    private <T> void updateField(T newValue, Consumer<T> updateAction, T existingValue, Consumer<T> responseSetter) {
        if (newValue != null) {
            updateAction.accept(newValue);
            responseSetter.accept(newValue);
        } else {
            responseSetter.accept(existingValue);
        }
    }

    public UserGrade getUserGrade(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        User user = optionalUser.get();
        return user.getGrade();
    }

    public List<User> getStaffs(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        Publisher publisher = publisherService.getPublisherByName(optionalUser.get().getPublisher().getName());
        return userRepository.findByPublisher(publisher);
    }

    public List<User> getStaffsByUserGrade(String email, UserGrade userGrade) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }

        Publisher publisher = publisherService.getPublisherByName(optionalUser.get().getPublisher().getName());
        return userRepository.findByPublisherAndGrade(publisher, userGrade);
    }

    public List<String> getCustomKeywords(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        return user.getCustomKeywords();

    }

    public Object clearCustomKeywords(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new BusinessException(ExceptionCode.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.getCustomKeywords().clear();
        userRepository.save(user);

        return StateResponse.builder().code("사용자 키워드 초기화").message("clear").build();
    }
}
