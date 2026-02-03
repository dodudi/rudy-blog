package kr.it.rudy.blog.post.application.dto;

import kr.it.rudy.blog.post.domain.PostStatus;

public record SearchPostRequest(
        PostStatus status,
        String author,
        String categorySlug,
        String tagSlug
) {
}
