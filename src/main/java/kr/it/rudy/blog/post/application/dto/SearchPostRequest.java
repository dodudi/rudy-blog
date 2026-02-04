package kr.it.rudy.blog.post.application.dto;

import kr.it.rudy.blog.post.domain.PostStatus;

public record SearchPostRequest(
        PostStatus status,
        String author,
        String categorySlug,
        String tagSlug,
        String categoryId,
        String tagId
) {
    public SearchPostRequest withCategoryId(String categoryId) {
        return new SearchPostRequest(status, author, categorySlug, tagSlug, categoryId, tagId);
    }

    public SearchPostRequest withTagId(String tagId) {
        return new SearchPostRequest(status, author, categorySlug, tagSlug, categoryId, tagId);
    }
}
