package kr.it.rudy.blog.tag.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Tag 수정 요청 DTO
 */
public record UpdateTagRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    String name,

    @NotBlank(message = "Slug is required")
    @Size(max = 50, message = "Slug cannot exceed 50 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can only contain lowercase letters, numbers, and hyphens")
    String slug
) {
}
