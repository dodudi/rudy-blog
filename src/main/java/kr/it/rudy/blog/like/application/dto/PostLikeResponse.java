package kr.it.rudy.blog.like.application.dto;

/**
 * PostLike 응답 DTO
 */
public record PostLikeResponse(
        String postId,
        long likeCount,
        boolean liked
) {
    public static PostLikeResponse of(String postId, long likeCount, boolean liked) {
        return new PostLikeResponse(postId, likeCount, liked);
    }
}
