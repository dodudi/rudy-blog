package kr.it.rudy.blog.bookmark.application.dto;

import kr.it.rudy.blog.bookmark.domain.PostBookmark;

import java.time.Instant;

/**
 * PostBookmark 응답 DTO
 */
public record PostBookmarkResponse(
        String id,
        String postId,
        String userId,
        boolean bookmarked,
        Instant createdAt
) {
    public static PostBookmarkResponse of(String postId, boolean bookmarked) {
        return new PostBookmarkResponse(null, postId, null, bookmarked, null);
    }

    public static PostBookmarkResponse from(PostBookmark bookmark) {
        return new PostBookmarkResponse(
                bookmark.getId().getValue(),
                bookmark.getPostId().getValue(),
                bookmark.getUserId(),
                true,
                bookmark.getCreatedDt()
        );
    }
}
