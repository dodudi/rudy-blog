package kr.it.rudy.blog.category.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.blog.category.application.dto.CategoryResponse;
import kr.it.rudy.blog.category.application.dto.CreateCategoryRequest;
import kr.it.rudy.blog.category.application.dto.UpdateCategoryRequest;
import kr.it.rudy.blog.category.application.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Category REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        CategoryResponse response = categoryService.createCategory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String id) {
        CategoryResponse response = categoryService.getCategory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse response = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> responses = categoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody UpdateCategoryRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        CategoryResponse response = categoryService.updateCategory(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");
        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");
        categoryService.deleteCategory(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }
}
