package kr.it.rudy.blog.like.domain;

import kr.it.rudy.blog.post.domain.PostId;

import java.util.Optional;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    Optional<PostLike> findByPostIdAndUserId(PostId postId, String userId);

    void delete(PostLikeId id);

    long countByPostId(PostId postId);

    boolean existsByPostIdAndUserId(PostId postId, String userId);
}
