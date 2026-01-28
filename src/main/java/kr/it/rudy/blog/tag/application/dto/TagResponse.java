package kr.it.rudy.blog.tag.application.dto;

import kr.it.rudy.blog.tag.domain.Tag;

import java.time.Instant;

/**
 * Tag 응답 DTO
 */
public record TagResponse(
        String id,
        String name,
        String slug,
        Instant createdAt,
        Instant updatedAt
) {
    public static TagResponse from(Tag tag) {
        return new TagResponse(
                tag.getId().getValue(),
                tag.getName(),
                tag.getSlug(),
                tag.getCreatedAt(),
                tag.getUpdatedAt()
        );
    }
}
