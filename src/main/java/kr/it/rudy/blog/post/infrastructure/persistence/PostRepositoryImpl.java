package kr.it.rudy.blog.post.infrastructure.persistence;

import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import kr.it.rudy.blog.post.domain.PostStatus;
import kr.it.rudy.blog.tag.domain.TagId;
import org.springframework.data.domain.Sort;
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
    public List<Post> findAll(Sort title) {
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
    public List<Post> findByAuthorAndStatus(String author, PostStatus status) {
        return jpaRepository.findByAuthorAndStatus(author, status).stream()
                .map(PostJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByCategoryId(CategoryId categoryId) {
        return jpaRepository.findByCategoryId(categoryId.getValue()).stream()
                .map(PostJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Post> findByTagId(TagId tagId) {
        return jpaRepository.findByTagId(tagId.getValue()).stream()
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
