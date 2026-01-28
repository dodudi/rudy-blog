package kr.it.rudy.blog.tag.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Tag의 식별자를 나타내는 Value Object
 */
public class TagId {
    private final String value;

    private TagId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TagId cannot be null or empty");
        }
        this.value = value;
    }

    public static TagId of(String value) {
        return new TagId(value);
    }

    public static TagId generate() {
        return new TagId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagId tagId = (TagId) o;
        return Objects.equals(value, tagId.value);
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
