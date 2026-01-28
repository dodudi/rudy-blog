package kr.it.rudy.blog.post.domain;

import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.tag.domain.TagId;
import lombok.Getter;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Post Aggregate Root
 */
@Getter
public class Post {
    private final PostId id;
    private String title;
    private String content;
    private String summary;
    private final String author;
    private PostStatus status;
    private CategoryId categoryId;
    private Set<TagId> tagIds;
    private Instant createdDt;
    private Instant updatedDt;

    private Post(PostId id, String title, String content, String summary, String author,
                 PostStatus status, CategoryId categoryId, Set<TagId> tagIds,
                 Instant createdDt, Instant updatedDt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.author = author;
        this.status = status;
        this.categoryId = categoryId;
        this.tagIds = tagIds != null ? new HashSet<>(tagIds) : new HashSet<>();
        this.createdDt = createdDt;
        this.updatedDt = updatedDt;
    }

    public static Post create(String title, String content, String author) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);

        return new Post(
                PostId.generate(),
                title,
                content,
                null,
                author,
                PostStatus.DRAFT,
                null,
                new HashSet<>(),
                null,
                null
        );
    }

    public static Post create(String title, String content, String summary, String author,
                              CategoryId categoryId, Set<TagId> tagIds) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);
        validateSummary(summary);

        return new Post(
                PostId.generate(),
                title,
                content,
                summary,
                author,
                PostStatus.DRAFT,
                categoryId,
                tagIds,
                null,
                null
        );
    }

    public static Post reconstitute(PostId id, String title, String content, String author,
                                    PostStatus status, Instant createdAt, Instant updatedAt) {
        return new Post(id, title, content, null, author, status, null, new HashSet<>(), createdAt, updatedAt);
    }

    public static Post reconstitute(PostId id, String title, String content, String summary, String author,
                                    PostStatus status, CategoryId categoryId, Set<TagId> tagIds,
                                    Instant createdAt, Instant updatedAt) {
        return new Post(id, title, content, summary, author, status, categoryId, tagIds, createdAt, updatedAt);
    }

    public void updateTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void updateContent(String content) {
        validateContent(content);
        this.content = content;
    }

    public void updateSummary(String summary) {
        validateSummary(summary);
        this.summary = summary;
    }

    public void updateCategory(CategoryId categoryId) {
        this.categoryId = categoryId;
    }

    public void updateTags(Set<TagId> tagIds) {
        this.tagIds = tagIds != null ? new HashSet<>(tagIds) : new HashSet<>();
    }

    public Set<TagId> getTagIds() {
        return Collections.unmodifiableSet(tagIds);
    }

    public void publish() {
        if (this.status == PostStatus.PUBLISHED) {
            throw new IllegalStateException("Post is already published");
        }
        this.status = PostStatus.PUBLISHED;
    }

    public void unpublish() {
        if (this.status == PostStatus.DRAFT) {
            throw new IllegalStateException("Post is already draft");
        }
        this.status = PostStatus.DRAFT;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Title cannot exceed 200 characters");
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
    }

    private static void validateAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
    }

    private static void validateSummary(String summary) {
        if (summary != null && summary.length() > 300) {
            throw new IllegalArgumentException("Summary cannot exceed 300 characters");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
