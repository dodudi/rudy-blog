package kr.it.rudy.blog.post.infrastructure.persistence;

import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import kr.it.rudy.blog.post.domain.PostStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Post> findById(PostId id) {
        return jpaRepository.findById(id.getValue())
                .map(PostJpaEntity::toDomain);
    }

    @Override
    public List<Post> findAll() {
        return jpaRepository.findAll().stream()
                .map(PostJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByStatus(PostStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(PostJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByAuthor(String author) {
        return jpaRepository.findByAuthor(author).stream()
                .map(PostJpaEntity::toDomain)
                .collect(Collectors.toList());
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
