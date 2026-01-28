package kr.it.rudy.blog.category.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Category의 식별자를 나타내는 Value Object
 */
public class CategoryId {
    private final String value;

    private CategoryId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CategoryId cannot be null or empty");
        }
        this.value = value;
    }

    public static CategoryId of(String value) {
        return new CategoryId(value);
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
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
