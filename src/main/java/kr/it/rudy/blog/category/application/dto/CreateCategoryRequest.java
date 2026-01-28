package kr.it.rudy.blog.category.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Category 생성 요청 DTO
 */
public record CreateCategoryRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,

    @NotBlank(message = "Slug is required")
    @Size(max = 100, message = "Slug cannot exceed 100 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers, and hyphens")
    String slug,

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    String description
) {
}
