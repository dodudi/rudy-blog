package kr.it.rudy.blog.comment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Comment 수정 요청 DTO
 */
public record UpdateCommentRequest(
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    String content
) {
}
