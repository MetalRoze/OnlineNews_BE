package com.example.onlinenews.article.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleResponseDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Article;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.article.repository.ArticleRepository;
import com.example.onlinenews.article_img.entity.ArticleImg;
import com.example.onlinenews.article_img.repository.ArticleImgRepository;
import com.example.onlinenews.error.BusinessException;
import com.example.onlinenews.error.ExceptionCode;
import com.example.onlinenews.notification.service.NotificationService;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Autowired
    private final AmazonS3 amazonS3;
    private final ArticleImgRepository articleImgRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 기사 작성
    public ResponseEntity<String> createArticle(ArticleRequestDTO requestDTO, String email, List<MultipartFile> images) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        // Article 엔티티 생성
        Article article = Article.builder()
                .user(user)
                .category(requestDTO.getCategory())
                .title(requestDTO.getTitle())
                .subtitle(requestDTO.getSubtitle())
                .content(requestDTO.getContent())
                .state(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .isPublic(false)
                .build();

        // Article을 먼저 저장하여 ID를 생성
        Article savedArticle = articleRepository.save(article);

        if (images != null && !images.isEmpty()) {
            List<ArticleImg> articleImgs = new ArrayList<>();
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new BusinessException(ExceptionCode.FILE_NOT_FOUND);
                }
                String fileUrl=saveImg(file);

                ArticleImg articleImg = ArticleImg.builder()
                        .article(savedArticle)
                        .imgUrl(fileUrl)
                        .build();
                articleImgs.add(articleImg);
            }

            // ArticleImg 엔티티 저장
            articleImgRepository.saveAll(articleImgs);
        }

        return ResponseEntity.ok("기사가 제출되었습니다. 승인을 기다려 주세요!");
    }


    // 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();

        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(this::convertToResponseDTO) // 변환 메서드 호출
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }


    // 기사 상세 조회
    @Transactional
    public ResponseEntity<ArticleResponseDTO> getArticleById(Long id) {
        incrementViewCount(id); // 조회수 증가
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        return ResponseEntity.ok(convertToResponseDTO(article));
    }

    // 카테고리 별 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> listArticlesByCategory(Category category) {
        List<Article> articles = articleRepository.findByCategory(category);
        List<ArticleResponseDTO> responseDTOS = articles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOS);
    }

    // 제목 또는 내용을 포함하는 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> searchArticles(String title, String content) {
        List<Article> articles = articleRepository.findByTitleContainingOrContentContaining(title, content);
        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // 사용자 ID로 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> getArticlesByUserId(Long userId) {
        List<Article> articles = articleRepository.findByUserId(userId);
        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // 공개된 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> getPublicArticles() {
        List<Article> articles = articleRepository.findByIsPublicTrue();
        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // 상태에 따른 기사 목록 조회
    @Transactional
    public ResponseEntity<List<ArticleResponseDTO>> getArticlesByState(RequestStatus state) {
        List<Article> articles = articleRepository.findByState(state); // 상태에 따라 기사 조회
        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(this::convertToResponseDTO) // DTO로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs); // 변환된 DTO 리스트 반환
    }

    // 기사 수정
    @Transactional
    public ResponseEntity<ArticleResponseDTO> updateArticle(Long id, ArticleUpdateRequestDTO updateRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        if (updateRequest.getTitle() != null) {
            article.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getSubtitle() != null) {
            article.setSubtitle(updateRequest.getSubtitle());
        }
        if (updateRequest.getContent() != null) {
            article.setContent(updateRequest.getContent());
        }
        if (updateRequest.getCategory() != null) {
            article.setCategory(updateRequest.getCategory());
        }
        if (updateRequest.getImages() != null) {
            article.setImages(updateRequest.getImages());
        }
        if (updateRequest.getIsPublic() != null) {
            article.setIsPublic(updateRequest.getIsPublic());
        }

        article.setModifiedAt(LocalDateTime.now());

        Article updatedArticle = articleRepository.save(article); // 변경사항 저장
        return ResponseEntity.ok(convertToResponseDTO(updatedArticle));
    }

    public void incrementViewCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        article.setViews(article.getViews() + 1); // 조회수 증가
        articleRepository.save(article); // 변경사항 저장
    }

    //편집장 요청 처리 시 수정됨
    public void statusUpdate(Long articleId, RequestStatus newRequestStatus){
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        article.updateStatue(newRequestStatus);
    }

    public void create(){
        notificationService.createRequestNoti(2L,2L);
    }


    private ArticleResponseDTO convertToResponseDTO(Article article) {
        List<ArticleImg> images = article.getImages();
        if (images == null) {
            images = new ArrayList<>(); // 빈 리스트로 초기화
        }

        return ArticleResponseDTO.builder()
                .id(article.getId())
                .title(article.getTitle())
                .subtitle(article.getSubtitle())
                .content(article.getContent())
                .category(article.getCategory())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .holdAt(article.getHoldAt())
                .approvedAt(article.getApprovedAt())
                .state(article.getState())
                .isPublic(article.getIsPublic())
                .views(article.getViews())
                .images(images.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()))
                .build();
    }


    public String saveImg(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && !originalFilename.isEmpty()) {
            fileExtension = "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFilename = UUID.randomUUID() + fileExtension;
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/articleImg/" + uniqueFilename;

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, "articleImg/" + uniqueFilename, file.getInputStream(), null));
        } catch (IOException e) {
            throw new BusinessException(ExceptionCode.S3_UPLOAD_FAILED);
        }
        return fileUrl;
    }

}
