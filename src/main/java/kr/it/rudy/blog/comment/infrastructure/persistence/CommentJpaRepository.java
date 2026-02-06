package kr.it.rudy.blog.comment.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, String> {

    List<CommentJpaEntity> findByPostIdOrderByCreatedDtAsc(String postId);

    List<CommentJpaEntity> findByParentId(String parentId);
}
