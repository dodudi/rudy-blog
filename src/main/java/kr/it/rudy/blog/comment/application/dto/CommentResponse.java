package kr.it.rudy.blog.comment.application.dto;

import kr.it.rudy.blog.comment.domain.Comment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Comment 응답 DTO
 */
public record CommentResponse(
        String id,
        String postId,
        String parentId,
        String content,
        String author,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> replies
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId().getValue(),
                comment.getPostId().getValue(),
                comment.getParentId() != null ? comment.getParentId().getValue() : null,
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedDt(),
                comment.getUpdatedDt(),
                new ArrayList<>()
        );
    }

    public static CommentResponse from(Comment comment, List<CommentResponse> replies) {
        return new CommentResponse(
                comment.getId().getValue(),
                comment.getPostId().getValue(),
                comment.getParentId() != null ? comment.getParentId().getValue() : null,
                comment.getContent(),
                comment.getAuthor(),
                comment.getCreatedDt(),
                comment.getUpdatedDt(),
                replies != null ? replies : new ArrayList<>()
        );
    }
}
