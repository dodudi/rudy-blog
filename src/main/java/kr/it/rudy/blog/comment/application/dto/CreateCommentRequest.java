package kr.it.rudy.blog.comment.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Comment 생성 요청 DTO
 */
public record CreateCommentRequest(
    @NotBlank(message = "Content is required")
    @Size(max = 1000, message = "Content cannot exceed 1000 characters")
    String content,

    @NotBlank(message = "Author is required")
    String author,

    String parentId
) {
}
