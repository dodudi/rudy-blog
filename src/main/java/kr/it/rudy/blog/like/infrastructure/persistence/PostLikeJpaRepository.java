package kr.it.rudy.blog.like.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLikeJpaEntity, String> {

    Optional<PostLikeJpaEntity> findByPostIdAndUserId(String postId, String userId);

    void deleteByPostId(String postId);

    long countByPostId(String postId);

    boolean existsByPostIdAndUserId(String postId, String userId);
}
