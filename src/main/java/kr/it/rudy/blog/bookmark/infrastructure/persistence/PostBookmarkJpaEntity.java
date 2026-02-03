package kr.it.rudy.blog.bookmark.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.bookmark.domain.PostBookmark;
import kr.it.rudy.blog.bookmark.domain.PostBookmarkId;
import kr.it.rudy.blog.post.domain.PostId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post_bookmarks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
public class PostBookmarkJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @CreatedDate
    @Column(name = "created_dt", nullable = false, updatable = false)
    private Instant createdDt;

    private PostBookmarkJpaEntity(String id, String postId, String userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    public static PostBookmarkJpaEntity fromDomain(PostBookmark postBookmark) {
        return new PostBookmarkJpaEntity(
                postBookmark.getId().getValue(),
                postBookmark.getPostId().getValue(),
                postBookmark.getUserId()
        );
    }

    public PostBookmark toDomain() {
        return PostBookmark.reconstitute(
                PostBookmarkId.of(this.id),
                PostId.of(this.postId),
                this.userId,
                this.createdDt
        );
    }
}
