package kr.it.rudy.blog.bookmark.infrastructure.persistence;

import kr.it.rudy.blog.bookmark.domain.PostBookmark;
import kr.it.rudy.blog.bookmark.domain.PostBookmarkId;
import kr.it.rudy.blog.bookmark.domain.PostBookmarkRepository;
import kr.it.rudy.blog.post.domain.PostId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostBookmarkRepositoryImpl implements PostBookmarkRepository {

    private final PostBookmarkJpaRepository jpaRepository;

    public PostBookmarkRepositoryImpl(PostBookmarkJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PostBookmark save(PostBookmark postBookmark) {
        PostBookmarkJpaEntity entity = PostBookmarkJpaEntity.fromDomain(postBookmark);
        PostBookmarkJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<PostBookmark> findByPostIdAndUserId(PostId postId, String userId) {
        return jpaRepository.findByPostIdAndUserId(postId.getValue(), userId)
                .map(PostBookmarkJpaEntity::toDomain);
    }

    @Override
    public List<PostBookmark> findByUserId(String userId) {
        return jpaRepository.findByUserIdOrderByCreatedDtDesc(userId).stream()
                .map(PostBookmarkJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(PostBookmarkId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    @Transactional
    public void deleteByPostId(PostId postId) {
        jpaRepository.deleteByPostId(postId.getValue());
    }

    @Override
    public boolean existsByPostIdAndUserId(PostId postId, String userId) {
        return jpaRepository.existsByPostIdAndUserId(postId.getValue(), userId);
    }
}
