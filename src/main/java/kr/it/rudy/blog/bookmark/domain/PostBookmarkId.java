package kr.it.rudy.blog.bookmark.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * PostBookmark의 식별자를 나타내는 Value Object
 */
public class PostBookmarkId {
    private final String value;

    private PostBookmarkId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PostBookmarkId cannot be null or empty");
        }
        this.value = value;
    }

    public static PostBookmarkId of(String value) {
        return new PostBookmarkId(value);
    }

    public static PostBookmarkId generate() {
        return new PostBookmarkId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostBookmarkId that = (PostBookmarkId) o;
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
