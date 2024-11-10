package com.example.onlinenews.article.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
import com.example.onlinenews.like.repository.ArticleLikeRepository;
import com.example.onlinenews.like.service.ArticleLikeService;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.service.RequestService;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;
    private final ArticleLikeService articleLikeService;

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

        // 이미지 저장
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) {
                    throw new BusinessException(ExceptionCode.FILE_NOT_FOUND);
                }
                imageUrls.add(saveImg(file));
            }
            article.setContent(replaceImgUrl(requestDTO.getContent(), imageUrls));
        }

        // Article을 먼저 저장하여 ID를 생성
        Article savedArticle = articleRepository.save(article);

        // Article_Img 저장
        if (!imageUrls.isEmpty()) {
            List<ArticleImg> articleImgs = new ArrayList<>();
            for (String url : imageUrls) {
                ArticleImg articleImg = ArticleImg.builder()
                        .article(savedArticle)
                        .imgUrl(url)
                        .build();
                articleImgs.add(articleImg);}

            articleImgRepository.saveAll(articleImgs);
        }

        //그냥 기자 밑에 인턴기자, 시민기자들은 요청 받아야함
        if(user.getGrade().getValue() < UserGrade.REPORTER.getValue()){
            requestService.create(user, savedArticle);
        }
        return ResponseEntity.ok("기사가 제출되었습니다. 승인을 기다려 주세요!");
    }

    // 기사 검색
    @Transactional
    public ResponseEntity<?> getArticles(
            Long id,
            Category category,
            String title,
            String content,
            Long userId,
            RequestStatus state,
            Boolean isPublic,
            String sortBy,
            String sortDirection) {

        List<Article> articles;

        // 정렬 조건 처리
        Sort sort = null;
        if (sortBy != null && sortDirection != null) {
            if ("asc".equalsIgnoreCase(sortDirection)) {
                sort = Sort.by(Sort.Order.asc(sortBy));
            } else if ("desc".equalsIgnoreCase(sortDirection)) {
                sort = Sort.by(Sort.Order.desc(sortBy));
            }
        }

        if (id != null) {
            // 기사 상세 보기
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
            incrementViewCount(id); // 조회수 증가
            articles = List.of(article);
        } else {
            // 복합 조건 조회
            if (sort != null) {
                // 정렬 기준이 있을 경우, 해당 조건을 적용하여 조회
                articles = articleRepository.findAll(sort).stream()
                        .filter(article -> category == null || article.getCategory().equals(category))
                        .filter(article -> title == null || article.getTitle().contains(title))
                        .filter(article -> content == null || article.getContent().contains(content))
                        .filter(article -> userId == null || (article.getUser() != null && Objects.equals(article.getUser().getId(), userId)))
                        .filter(article -> state == null || article.getState().equals(state))
                        .filter(article -> isPublic == null || article.getIsPublic().equals(isPublic))
                        .collect(Collectors.toList());
            } else {
                // 정렬 기준이 없으면 모든 데이터 보여줌
                articles = articleRepository.findAll().stream()
                        .filter(article -> category == null || article.getCategory().equals(category))
                        .filter(article -> title == null || article.getTitle().contains(title))
                        .filter(article -> content == null || article.getContent().contains(content))
                        .filter(article -> userId == null || (article.getUser() != null && Objects.equals(article.getUser().getId(), userId)))
                        .filter(article -> state == null || article.getState().equals(state))
                        .filter(article -> isPublic == null || article.getIsPublic().equals(isPublic))
                        .collect(Collectors.toList());
            }
        }

        List<ArticleResponseDTO> responseDTOs = articles.stream()
                .map(article -> {
                    ArticleResponseDTO dto = convertToResponseDTO(article);
                    int likesCount = articleLikeService.articleLikes(article.getId()).size(); // 좋아요 개수 가져오기
                    dto.setLikes(likesCount); // DTO에 좋아요 개수 설정
                    return dto;
                })
                .collect(Collectors.toList());

        if (articles.isEmpty()) {
            return ResponseEntity.ok("검색 결과가 없습니다.");
        }
        return ResponseEntity.ok(responseDTOs);
    }

    // 기사 수정
    @Transactional
    public ResponseEntity<?> updateArticle(Long id, ArticleUpdateRequestDTO updateRequest, List<MultipartFile> images ) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        if (updateRequest.getTitle() != null) {
            article.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getSubtitle() != null) {
            article.setSubtitle(updateRequest.getSubtitle());
        }
        if (updateRequest.getCategory() != null) {
            article.setCategory(updateRequest.getCategory());
        }
        if (updateRequest.getIsPublic() != null) {
            article.setIsPublic(updateRequest.getIsPublic());
        }

        if (updateRequest.getContent() != null) {

            List<String> imageUrls = new ArrayList<>();
            if (images != null && !images.isEmpty()) {
                for (MultipartFile file : images) {
                    if (file.isEmpty()) {
                        throw new BusinessException(ExceptionCode.FILE_NOT_FOUND);
                    }
                    imageUrls.add(saveImg(file));

                    List<ArticleImg> articleImgs = new ArrayList<>();
                    for (String url : imageUrls) {
                        ArticleImg articleImg = ArticleImg.builder()
                                .article(article)
                                .imgUrl(url)
                                .build();
                        articleImgs.add(articleImg);
                        articleImgRepository.saveAll(articleImgs);
                    }
                }
                article.setContent(replaceImgUrl(updateRequest.getContent(), imageUrls));

            }
            else article.setContent(updateRequest.getContent());
        }

        // 이미지 삭제
        List<String> deleteImages = updateRequest.getDeleteImages();
        if (deleteImages != null && !deleteImages.isEmpty()) {
            for (String imgUrl : deleteImages) {
                deleteImg(imgUrl);
                articleImgRepository.deleteByImgUrl(imgUrl);
            }
        }

        article.setModifiedAt(LocalDateTime.now());
        articleRepository.save(article);

        return ResponseEntity.ok("기사가 수정되었습니다. 편집장의 승인 후 게시됩니다.");
    }

    // 조회수 처리
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

    public void deleteImg(String imgUrl) {
        try {
            String fileName = extractFileNameFromUrl(imgUrl);
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
                    bucketName,
                    "articleImg/" + fileName
            );

            // S3에서 이미지 삭제
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            System.out.println("Error during delete operation: " + e.getMessage());
            throw new BusinessException(ExceptionCode.FILE_DELETE_FAILED);
        }
    }

    public String extractFileNameFromUrl(String url) {
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }

    public String replaceImgUrl(String content, List<String> imgUrls) {
        Document document = Jsoup.parse(content);
        List<Element> images = document.select("img");

        int urlIndex = 0;
        for (Element img : images) {
            if (urlIndex < imgUrls.size()) {
                img.attr("src", imgUrls.get(urlIndex));
                urlIndex++;
            } else {
                break;
            }
        }
        return document.body().html();
    }

}
