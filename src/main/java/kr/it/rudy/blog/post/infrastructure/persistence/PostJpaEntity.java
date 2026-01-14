package kr.it.rudy.blog.post.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "author", nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status;

    private PostJpaEntity(String id, String title, String content, String author,
                          PostStatus status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
    }

    public static PostJpaEntity fromDomain(Post post) {
        return new PostJpaEntity(
                post.getId().getValue(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getStatus()
        );
    }

    public Post toDomain() {
        return Post.reconstitute(
                PostId.of(this.id),
                this.title,
                this.content,
                this.author,
                this.status,
                this.getCreatedAt(),
                this.getUpdatedAt()
        );
    }
}
