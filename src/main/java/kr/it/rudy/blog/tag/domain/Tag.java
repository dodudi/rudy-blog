package kr.it.rudy.blog.tag.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Tag Aggregate Root
 */
@Getter
public class Tag {
    private final TagId id;
    private String name;
    private String slug;
    private Instant createdAt;
    private Instant updatedAt;

    private Tag(TagId id, String name, String slug, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Tag create(String name, String slug) {
        validateName(name);
        validateSlug(slug);

        return new Tag(
                TagId.generate(),
                name,
                slug,
                null,
                null
        );
    }

    public static Tag reconstitute(TagId id, String name, String slug,
                                    Instant createdAt, Instant updatedAt) {
        return new Tag(id, name, slug, createdAt, updatedAt);
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public void updateSlug(String slug) {
        validateSlug(slug);
        this.slug = slug;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be null or empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Tag name cannot exceed 50 characters");
        }
    }

    private static void validateSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Tag slug cannot be null or empty");
        }
        if (slug.length() > 50) {
            throw new IllegalArgumentException("Tag slug cannot exceed 50 characters");
        }
        if (!slug.matches("^[a-z0-9-]+$")) {
            throw new IllegalArgumentException("Tag slug can only contain lowercase letters, numbers, and hyphens");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
