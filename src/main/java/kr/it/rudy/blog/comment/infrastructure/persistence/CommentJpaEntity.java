package kr.it.rudy.blog.comment.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.comment.domain.Comment;
import kr.it.rudy.blog.comment.domain.CommentId;
import kr.it.rudy.blog.common.infrastructure.persistence.BaseEntity;
import kr.it.rudy.blog.post.domain.PostId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class CommentJpaEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "author", nullable = false)
    private String author;

    private CommentJpaEntity(String id, String postId, String parentId, String content, String author) {
        this.id = id;
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;
        this.author = author;
    }

    public static CommentJpaEntity fromDomain(Comment comment) {
        return new CommentJpaEntity(
                comment.getId().getValue(),
                comment.getPostId().getValue(),
                comment.getParentId() != null ? comment.getParentId().getValue() : null,
                comment.getContent(),
                comment.getAuthor()
        );
    }

    public Comment toDomain() {
        return Comment.reconstitute(
                CommentId.of(this.id),
                PostId.of(this.postId),
                this.parentId != null ? CommentId.of(this.parentId) : null,
                this.content,
                this.author,
                this.getCreatedDt(),
                this.getUpdatedDt()
        );
    }
}
