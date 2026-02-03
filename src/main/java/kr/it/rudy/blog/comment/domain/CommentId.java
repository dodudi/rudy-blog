package kr.it.rudy.blog.comment.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Comment의 식별자를 나타내는 Value Object
 */
public class CommentId {
    private final String value;

    private CommentId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CommentId cannot be null or empty");
        }
        this.value = value;
    }

    public static CommentId of(String value) {
        return new CommentId(value);
    }

    public static CommentId generate() {
        return new CommentId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentId commentId = (CommentId) o;
        return Objects.equals(value, commentId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
