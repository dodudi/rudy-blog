package kr.it.rudy.blog.comment.domain;

import kr.it.rudy.blog.post.domain.PostId;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Comment Aggregate Root
 */
@Getter
public class Comment {
    private final CommentId id;
    private final PostId postId;
    private final CommentId parentId;
    private String content;
    private final String author;
    private Instant createdDt;
    private Instant updatedDt;

    private Comment(CommentId id, PostId postId, CommentId parentId, String content, String author,
                    Instant createdDt, Instant updatedDt) {
        this.id = id;
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;
        this.author = author;
        this.createdDt = createdDt;
        this.updatedDt = updatedDt;
    }

    public static Comment create(PostId postId, String content, String author) {
        validatePostId(postId);
        validateContent(content);
        validateAuthor(author);

        return new Comment(
                CommentId.generate(),
                postId,
                null,
                content,
                author,
                null,
                null
        );
    }

    public static Comment createReply(PostId postId, CommentId parentId, String content, String author) {
        validatePostId(postId);
        validateParentId(parentId);
        validateContent(content);
        validateAuthor(author);

        return new Comment(
                CommentId.generate(),
                postId,
                parentId,
                content,
                author,
                null,
                null
        );
    }

    public static Comment reconstitute(CommentId id, PostId postId, CommentId parentId, String content,
                                        String author, Instant createdDt, Instant updatedDt) {
        return new Comment(id, postId, parentId, content, author, createdDt, updatedDt);
    }

    public void updateContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public boolean isReply() {
        return parentId != null;
    }

    public boolean isOwnedBy(String author) {
        return this.author.equals(author);
    }

    private static void validatePostId(PostId postId) {
        if (postId == null) {
            throw new IllegalArgumentException("PostId cannot be null");
        }
    }

    private static void validateParentId(CommentId parentId) {
        if (parentId == null) {
            throw new IllegalArgumentException("ParentId cannot be null for reply");
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (content.length() > 1000) {
            throw new IllegalArgumentException("Content cannot exceed 1000 characters");
        }
    }

    private static void validateAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
