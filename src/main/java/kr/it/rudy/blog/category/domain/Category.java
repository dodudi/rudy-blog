package kr.it.rudy.blog.category.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Category Aggregate Root
 */
@Getter
public class Category {
    private final CategoryId id;
    private String name;
    private String slug;
    private String description;
    private String ownerId;
    private Instant createdAt;
    private Instant updatedAt;

    private Category(CategoryId id, String name, String slug, String description,
                     String ownerId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Category create(String name, String slug, String description, String ownerId) {
        validateName(name);
        validateSlug(slug);
        Objects.requireNonNull(ownerId, "Owner ID cannot be null");

        return new Category(
                CategoryId.generate(),
                name,
                slug,
                description,
                ownerId,
                null,
                null
        );
    }

    public static Category reconstitute(CategoryId id, String name, String slug, String description,
                                         String ownerId, Instant createdAt, Instant updatedAt) {
        return new Category(id, name, slug, description, ownerId, createdAt, updatedAt);
    }

    public boolean isOwner(String userId) {
        return ownerId != null && ownerId.equals(userId);
    }

    public void updateName(String name) {
        validateName(name);
        this.name = name;
    }

    public void updateSlug(String slug) {
        validateSlug(slug);
        this.slug = slug;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");
        }
    }

    private static void validateSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Category slug cannot be null or empty");
        }
        if (slug.length() > 100) {
            throw new IllegalArgumentException("Category slug cannot exceed 100 characters");
        }
        if (!slug.matches("^[a-z0-9-]+$")) {
            throw new IllegalArgumentException("Category slug can only contain lowercase letters, numbers, and hyphens");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
