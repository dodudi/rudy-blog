package kr.it.rudy.blog.post.application.dto;

import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostStatus;

import java.time.Instant;

/**
 * Post 응답 DTO
 */
public record PostResponse(
        String id,
        String title,
        String content,
        String author,
        PostStatus status,
        Instant createDt,
        Instant updateDt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId().getValue(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
