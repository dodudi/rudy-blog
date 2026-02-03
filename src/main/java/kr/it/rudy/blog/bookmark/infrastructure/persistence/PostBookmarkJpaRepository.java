package kr.it.rudy.blog.bookmark.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostBookmarkJpaRepository extends JpaRepository<PostBookmarkJpaEntity, String> {

    Optional<PostBookmarkJpaEntity> findByPostIdAndUserId(String postId, String userId);

    List<PostBookmarkJpaEntity> findByUserIdOrderByCreatedDtDesc(String userId);

    void deleteByPostId(String postId);

    boolean existsByPostIdAndUserId(String postId, String userId);
}
