package kr.it.rudy.blog.post.application.dto;

import kr.it.rudy.blog.category.application.dto.CategoryResponse;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostStatus;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.domain.TagId;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Post 응답 DTO
 */
public record PostResponse(
        String id,
        String title,
        String content,
        String summary,
        String author,
        PostStatus status,
        String categoryId,
        CategoryResponse category,
        Set<String> tagIds,
        List<TagResponse> tags,
        Instant createdAt,
        Instant updatedAt
) {
    public static PostResponse from(Post post) {
        Set<String> tagIdStrings = post.getTagIds().stream()
                .map(TagId::getValue)
                .collect(Collectors.toSet());

        return new PostResponse(
                post.getId().getValue(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getAuthor(),
                post.getStatus(),
                post.getCategoryId() != null ? post.getCategoryId().getValue() : null,
                null,
                tagIdStrings,
                null,
                post.getCreatedDt(),
                post.getUpdatedDt()
        );
    }

    public static PostResponse from(Post post, CategoryResponse category, List<TagResponse> tags) {
        Set<String> tagIdStrings = post.getTagIds().stream()
                .map(TagId::getValue)
                .collect(Collectors.toSet());

        return new PostResponse(
                post.getId().getValue(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getAuthor(),
                post.getStatus(),
                post.getCategoryId() != null ? post.getCategoryId().getValue() : null,
                category,
                tagIdStrings,
                tags,
                post.getCreatedDt(),
                post.getUpdatedDt()
        );
    }
}
