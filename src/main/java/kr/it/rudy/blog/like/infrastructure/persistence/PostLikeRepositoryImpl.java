package kr.it.rudy.blog.like.infrastructure.persistence;

import kr.it.rudy.blog.like.domain.PostLike;
import kr.it.rudy.blog.like.domain.PostLikeId;
import kr.it.rudy.blog.like.domain.PostLikeRepository;
import kr.it.rudy.blog.post.domain.PostId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostLikeRepositoryImpl implements PostLikeRepository {

    private final PostLikeJpaRepository jpaRepository;

    public PostLikeRepositoryImpl(PostLikeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PostLike save(PostLike postLike) {
        PostLikeJpaEntity entity = PostLikeJpaEntity.fromDomain(postLike);
        PostLikeJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<PostLike> findByPostIdAndUserId(PostId postId, String userId) {
        return jpaRepository.findByPostIdAndUserId(postId.getValue(), userId)
                .map(PostLikeJpaEntity::toDomain);
    }

    @Override
    public void delete(PostLikeId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public long countByPostId(PostId postId) {
        return jpaRepository.countByPostId(postId.getValue());
    }

    @Override
    public boolean existsByPostIdAndUserId(PostId postId, String userId) {
        return jpaRepository.existsByPostIdAndUserId(postId.getValue(), userId);
    }
}
