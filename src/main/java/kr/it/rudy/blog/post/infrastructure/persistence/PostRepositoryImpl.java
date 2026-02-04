package kr.it.rudy.blog.post.infrastructure.persistence;

import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJpaRepository jpaRepository;

    public PostRepositoryImpl(PostJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Post save(Post post) {
        PostJpaEntity entity = PostJpaEntity.fromDomain(post);
        PostJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Post update(Post post) {
        PostJpaEntity entity = PostJpaEntity.fromDomain(post);
        entity.setCreatedDt(post.getCreatedDt());
        PostJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Post> findById(PostId id) {
        return jpaRepository.findById(id.getValue())
                .map(PostJpaEntity::toDomain);
    }

    @Override
    public Page<Post> findAll(Specification<PostJpaEntity> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable)
                .map(PostJpaEntity::toDomain);
    }

    @Override
    public void delete(PostId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(PostId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
