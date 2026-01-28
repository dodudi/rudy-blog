package kr.it.rudy.blog.category.application.dto;

import kr.it.rudy.blog.category.domain.Category;

import java.time.Instant;

/**
 * Category 응답 DTO
 */
public record CategoryResponse(
        String id,
        String name,
        String slug,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static CategoryResponse from(Category category) {
        return new CategoryResponse(
                category.getId().getValue(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
