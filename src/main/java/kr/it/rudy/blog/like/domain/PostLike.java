package kr.it.rudy.blog.like.domain;

import kr.it.rudy.blog.post.domain.PostId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * PostLike Aggregate Root
 */
@Getter
public class PostLike {
    private final PostLikeId id;
    private final PostId postId;
    private final String userId;
    private Instant createdDt;

    private PostLike(PostLikeId id, PostId postId, String userId, Instant createdDt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdDt = createdDt;
    }

    public static PostLike create(PostId postId, String userId) {
        validatePostId(postId);
        validateUserId(userId);

        return new PostLike(
                PostLikeId.generate(),
                postId,
                userId,
                null
        );
    }

    public static PostLike reconstitute(PostLikeId id, PostId postId, String userId, Instant createdDt) {
        return new PostLike(id, postId, userId, createdDt);
    }

    private static void validatePostId(PostId postId) {
        if (postId == null) {
            throw new IllegalArgumentException("PostId cannot be null");
        }
    }

    private static void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLike postLike = (PostLike) o;
        return Objects.equals(id, postLike.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
