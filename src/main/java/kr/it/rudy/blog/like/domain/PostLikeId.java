package kr.it.rudy.blog.like.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * PostLike의 식별자를 나타내는 Value Object
 */
public class PostLikeId {
    private final String value;

    private PostLikeId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PostLikeId cannot be null or empty");
        }
        this.value = value;
    }

    public static PostLikeId of(String value) {
        return new PostLikeId(value);
    }

    public static PostLikeId generate() {
        return new PostLikeId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLikeId that = (PostLikeId) o;
        return Objects.equals(value, that.value);
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
