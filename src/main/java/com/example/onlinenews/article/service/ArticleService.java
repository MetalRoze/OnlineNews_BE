package com.example.onlinenews.article.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.onlinenews.article.dto.ArticleKeywordDTO;
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
import com.example.onlinenews.error.StateResponse;
import com.example.onlinenews.like.repository.ArticleLikeRepository;
import com.example.onlinenews.like.service.ArticleLikeService;
import com.example.onlinenews.request.entity.Request;
import com.example.onlinenews.request.entity.RequestStatus;
import com.example.onlinenews.request.repository.RequestRepository;
import com.example.onlinenews.request.service.RequestService;
import com.example.onlinenews.user.entity.User;
import com.example.onlinenews.user.entity.UserGrade;
import com.example.onlinenews.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RequestRepository requestRepository;

    @Autowired
    private final AmazonS3 amazonS3;
    private final ArticleImgRepository articleImgRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 기사 작성
    public ResponseEntity<Long> createArticle(ArticleRequestDTO requestDTO, String email, List<MultipartFile> images) {
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
        return ResponseEntity.ok(savedArticle.getId());
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

        //승인요청
        if(article.getUser().getGrade().getValue() < UserGrade.REPORTER.getValue()){
            requestService.create(article.getUser(), article);
        }

        return ResponseEntity.ok("기사가 수정되었습니다. 편집장의 승인 후 게시됩니다.");
    }

    // 조회수 처리
    public void incrementViewCount(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));

        article.setViews(article.getViews() + 1); // 조회수 증가
        articleRepository.save(article); // 변경사항 저장
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
                .userId(article.getUser().getId())
                .userEmail(article.getUser().getEmail())
                .userName(article.getUser().getName())
                .userBio(article.getUser().getBio())
                .userImg(article.getUser().getImg())
                .publisherId(article.getUser().getPublisher().getId())
                .publisherName(article.getUser().getPublisher().getName())
                .publisherUrl(article.getUser().getPublisher().getUrl())
                .publisherImage(article.getUser().getPublisher().getImg())
                .images(images.stream().map(img -> img.getImgUrl()).collect(Collectors.toList()))
                .build();
    }


    // 서버에 이미지 저장
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

    // 서버에서 이미지 삭제
    public void deleteImg(String imgUrl) {
        try {
            String fileName = extractFileNameFromUrl(imgUrl);
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
                    bucketName,
                    "articleImg/" + fileName
            );
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.FILE_DELETE_FAILED);
        }
    }
    public String extractFileNameFromUrl(String url) {
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }

    // 서버에 올라간 url으로 변경
    public String replaceImgUrl(String content, List<String> imgUrls) {
        Document document = Jsoup.parse(content);
        List<Element> images = document.select("img");

        int urlIndex = 0;
        for (Element img : images) {
            String imgSrc = img.attr("src"); // 현재 이미지의 src 값 가져오기

            // 버킷 URL이 포함된 경우 변경하지 않음
            if (imgSrc.contains("onlinen-news-bucket.s3.amazonaws.com")) {
                continue; // 버킷 URL이 포함된 경우, 교체하지 않고 다음 이미지로 넘어감
            }

            // 버킷 URL이 포함되지 않은 경우에만 새로운 URL로 교체
            if (urlIndex < imgUrls.size()) {
                img.attr("src", imgUrls.get(urlIndex));
                urlIndex++;
            } else {
                break;
            }
        }
        return document.body().html();
    }
    public StateResponse articleKeyword(Long id, ArticleKeywordDTO requestDTO){
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            // Article이 없으면 예외를 던진다.
            throw new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND);
        }

        Article article = optionalArticle.get();

        // 3. Article에 keyword 추가 (List<String>에 새 키워드를 추가)
        article.getKeywords().addAll(requestDTO.getKeyword());

        // 4. 수정된 Article 저장 (CascadeType.ALL로 인해 save는 사실 필수적이지 않지만, 명시적으로 호출)
        articleRepository.save(article);

        // 5. StateResponse 반환 (필요한 응답 형태로 수정)
        return StateResponse.builder().code("키워드 추출 후 저장").message("키워드 추출 후 저장성공").build();
    }

    public ResponseEntity<?> getKeywords(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(optionalArticle.isEmpty()){
            throw new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND);
        }

        Article article = optionalArticle.get();

        List<String> keywords = article.getKeywords();  // getKeywords() 메서드로 키워드 리스트 반환

        // 키워드가 비어있는 경우 처리
        if (keywords.isEmpty()) {
            return ResponseEntity.status(404).body("No keywords found for the article.");
        }

        // 정상적으로 키워드 리스트 반환
        return ResponseEntity.ok(keywords);
    }

    //승인된 요청은 공개 비공개 설정가능
    @Transactional
    public void convertToPrivate(String email, Long articleId) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        if (!article.getIsPublic()) {
            throw new BusinessException(ExceptionCode.ALREADY_PRIVATE);
        }
        Request request = requestRepository.findByArticleAndType(article,"비공개 요청").orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        request.updateStatus(RequestStatus.APPROVED, "");
        request.confirm();
        article.updateIsPublic(false);
    }
    @Transactional
    public void convertToPublic(String email, Long articleId){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        checkEditorPermission(user);

        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        if (article.getIsPublic()) {
            throw new BusinessException(ExceptionCode.ALREADY_PUBLIC);
        }
        Request request = requestRepository.findByArticleAndType(article, "공개 요청").orElseThrow(() -> new BusinessException(ExceptionCode.REQUEST_NOT_FOUND));
        request.updateStatus(RequestStatus.APPROVED, "");
        request.confirm();
        article.updateIsPublic(true);
    }

    public boolean getPublicStatus(Long articleId){
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND));
        return article.getIsPublic();
    }

    private void checkEditorPermission(User user) {
        if (user.getGrade().getValue() < UserGrade.EDITOR.getValue()) {
            throw new BusinessException(ExceptionCode.USER_NOT_ALLOWED);
        }
    }

    @Transactional
    public Object deleteKeywords(Long id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isEmpty()) {
            throw new BusinessException(ExceptionCode.ARTICLE_NOT_FOUND);
        }

        Article article = optionalArticle.get();

        // 키워드 초기화 (리셋)
        article.getKeywords().clear();

        // 변경사항을 저장 (JPA의 @Transactional로 인해 자동 플러시됨)
        articleRepository.save(article);

        return StateResponse.builder()
                .code("delete keywords")
                .message("키워드 초기화 완료")
                .build();
    }

}
