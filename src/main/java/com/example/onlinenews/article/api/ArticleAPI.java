package com.example.onlinenews.article.api;

import com.example.onlinenews.article.dto.ArticleRequestDTO;
import com.example.onlinenews.article.dto.ArticleResponseDTO;
import com.example.onlinenews.article.dto.ArticleUpdateRequestDTO;
import com.example.onlinenews.article.entity.Category;
import com.example.onlinenews.request.entity.RequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/article")
@Tag(name = "Article", description = "기사 관련 API")
public interface ArticleAPI {

    @PostMapping("write")
    @Operation(summary = "기사 작성", description = "새 기사를 작성합니다.")
    public ResponseEntity<?> createArticle( HttpServletRequest httpServletRequest,
                                                             @RequestPart("requestDTO") ArticleRequestDTO requestDTO,
                                                             @RequestPart(value = "images", required = false) List<MultipartFile> images) ;

    @GetMapping("selectAll")
    @Operation(summary = "기사 목록 조회", description = "모든 기사를 조회합니다.")
    ResponseEntity<List<ArticleResponseDTO>> getAllArticles();

    @GetMapping("select/{id}")
    @Operation(summary = "기사 상세 조회", description = "특정 기사를 조회합니다.")
    ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Long id);

    @GetMapping("/select/category/{category}")
    @Operation(summary = "카테고리 별 조회", description = "특정 카테고리에 해당하는 기사를 조회합니다.")
    ResponseEntity<List<ArticleResponseDTO>> listArticlesByCategory(@PathVariable Category category);

    @GetMapping("select/search")
    @Operation(summary = "기사 제목, 내용 검색", description = "기사 제목이나 내용을 검색합니다.")
    ResponseEntity<List<ArticleResponseDTO>> searchArticles(@RequestParam String title, @RequestParam String content);

    @GetMapping("/select/user/{userId}")
    @Operation(summary = "작성자로 조회", description = "특정 기자가 작성한 기사 조회")
    ResponseEntity<List<ArticleResponseDTO>> getArticlesByUserId(@PathVariable Long userId);

    @GetMapping("select/state/{state}")
    @Operation(summary = "상태 별 조회", description = "상태에 따른 기사를 조회합니다.")
    ResponseEntity<List<ArticleResponseDTO>> getArticlesByState(@PathVariable RequestStatus state);

    @GetMapping("select/public")
    @Operation(summary = "공개된 기사 조회", description = "공개된 기사를 조회합니다.")
    ResponseEntity<List<ArticleResponseDTO>> getPublicArticles();

    @PatchMapping("update/{id}")
    @Operation(summary = "기사 수정", description = "기사를 수정합니다.")
    ResponseEntity<?> updateArticle(@PathVariable Long id,
                                                     @RequestPart("requestDTO") ArticleUpdateRequestDTO updateRequest,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images);
}
