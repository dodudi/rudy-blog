package kr.it.rudy.blog.post.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Post의 식별자를 나타내는 Value Object
 */
public class PostId {
    private final String value;

    private PostId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PostId cannot be null or empty");
        }
        this.value = value;
    }

    public static PostId of(String value) {
        return new PostId(value);
    }

    public static PostId generate() {
        return new PostId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostId postId = (PostId) o;
        return Objects.equals(value, postId.value);
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
