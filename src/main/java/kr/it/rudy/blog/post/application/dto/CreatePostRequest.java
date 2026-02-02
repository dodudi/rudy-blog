package kr.it.rudy.blog.post.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Post 생성 요청 DTO
 */
public record CreatePostRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    String title,

    @NotBlank(message = "Content is required")
    String content,

    @Size(max = 300, message = "Summary cannot exceed 300 characters")
    String summary,

    @NotBlank(message = "Author is required")
    String author,

    String categoryId,

    Set<String> tagIds,

    Boolean publish
) {
}
