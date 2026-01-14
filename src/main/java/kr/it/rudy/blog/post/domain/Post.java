package kr.it.rudy.blog.post.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

/**
 * Post Aggregate Root
 */
@Getter
public class Post {
    private final PostId id;
    private String title;
    private String content;
    private final String author;
    private PostStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    private Post(PostId id, String title, String content, String author, PostStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Post create(String title, String content, String author) {
        validateTitle(title);
        validateContent(content);
        validateAuthor(author);

        return new Post(
                PostId.generate(),
                title,
                content,
                author,
                PostStatus.DRAFT,
                null,
                null
        );
    }

    public static Post reconstitute(PostId id, String title, String content, String author,
                                    PostStatus status, Instant createdAt, Instant updatedAt) {
        return new Post(id, title, content, author, status, createdAt, updatedAt);
    }

    public void updateTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public void updateContent(String content) {
        validateContent(content);
        this.content = content;
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
