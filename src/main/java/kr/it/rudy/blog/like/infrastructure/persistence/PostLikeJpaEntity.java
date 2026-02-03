package kr.it.rudy.blog.like.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.like.domain.PostLike;
import kr.it.rudy.blog.like.domain.PostLikeId;
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
@Table(name = "post_likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
public class PostLikeJpaEntity {

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

    private PostLikeJpaEntity(String id, String postId, String userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    public static PostLikeJpaEntity fromDomain(PostLike postLike) {
        return new PostLikeJpaEntity(
                postLike.getId().getValue(),
                postLike.getPostId().getValue(),
                postLike.getUserId()
        );
    }

    public PostLike toDomain() {
        return PostLike.reconstitute(
                PostLikeId.of(this.id),
                PostId.of(this.postId),
                this.userId,
                this.createdDt
        );
    }
}
