package kr.it.rudy.blog.admin.presentation.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        CategoryResponse response = categoryService.createCategory(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        CategoryResponse response = categoryService.updateCategoryAsAdmin(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategoryAsAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
