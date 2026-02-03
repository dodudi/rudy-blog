package kr.it.rudy.blog.bookmark.domain;

import kr.it.rudy.blog.post.domain.PostId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * PostBookmark Aggregate Root
 */
@Getter
public class PostBookmark {
    private final PostBookmarkId id;
    private final PostId postId;
    private final String userId;
    private Instant createdDt;

    private PostBookmark(PostBookmarkId id, PostId postId, String userId, Instant createdDt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdDt = createdDt;
    }

    public static PostBookmark create(PostId postId, String userId) {
        validatePostId(postId);
        validateUserId(userId);

        return new PostBookmark(
                PostBookmarkId.generate(),
                postId,
                userId,
                null
        );
    }

    public static PostBookmark reconstitute(PostBookmarkId id, PostId postId, String userId, Instant createdDt) {
        return new PostBookmark(id, postId, userId, createdDt);
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
        PostBookmark that = (PostBookmark) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
