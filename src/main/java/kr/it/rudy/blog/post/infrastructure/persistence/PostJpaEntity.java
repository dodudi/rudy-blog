package kr.it.rudy.blog.post.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.common.infrastructure.persistence.BaseEntity;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostStatus;
import kr.it.rudy.blog.tag.domain.TagId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "posts")
public class PostJpaEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "summary", length = 300)
    private String summary;

    @Column(name = "author", nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status;

    @Column(name = "pinned", nullable = false)
    private boolean pinned = false;

    @Column(name = "category_id")
    private String categoryId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag_id")
    private Set<String> tagIds = new HashSet<>();

    private PostJpaEntity(String id, String title, String content, String summary, String author,
                          PostStatus status, boolean pinned, String categoryId, Set<String> tagIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.author = author;
        this.status = status;
        this.pinned = pinned;
        this.categoryId = categoryId;
        this.tagIds = tagIds != null ? new HashSet<>(tagIds) : new HashSet<>();
    }

    public static PostJpaEntity fromDomain(Post post) {
        Set<String> tagIdStrings = post.getTagIds().stream()
                .map(TagId::getValue)
                .collect(Collectors.toSet());

        return new PostJpaEntity(
                post.getId().getValue(),
                post.getTitle(),
                post.getContent(),
                post.getSummary(),
                post.getAuthor(),
                post.getStatus(),
                post.isPinned(),
                post.getCategoryId() != null ? post.getCategoryId().getValue() : null,
                tagIdStrings
        );
    }

    public Post toDomain() {
        Set<TagId> tagIdSet = this.tagIds.stream()
                .map(TagId::of)
                .collect(Collectors.toSet());

        return Post.reconstitute(
                PostId.of(this.id),
                this.title,
                this.content,
                this.summary,
                this.author,
                this.status,
                this.pinned,
                this.categoryId != null ? CategoryId.of(this.categoryId) : null,
                tagIdSet,
                this.getCreatedDt(),
                this.getUpdatedDt()
        );
    }
}
