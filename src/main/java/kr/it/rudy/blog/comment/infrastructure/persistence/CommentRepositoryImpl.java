package kr.it.rudy.blog.comment.infrastructure.persistence;

import kr.it.rudy.blog.comment.domain.Comment;
import kr.it.rudy.blog.comment.domain.CommentId;
import kr.it.rudy.blog.comment.domain.CommentRepository;
import kr.it.rudy.blog.post.domain.PostId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    public CommentRepositoryImpl(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comment save(Comment comment) {
        CommentJpaEntity entity = CommentJpaEntity.fromDomain(comment);
        CommentJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Comment update(Comment comment) {
        CommentJpaEntity entity = CommentJpaEntity.fromDomain(comment);
        entity.setCreatedDt(comment.getCreatedDt());
        CommentJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Comment> findById(CommentId id) {
        return jpaRepository.findById(id.getValue())
                .map(CommentJpaEntity::toDomain);
    }

    @Override
    public List<Comment> findByPostId(PostId postId) {
        return jpaRepository.findByPostIdOrderByCreatedDtAsc(postId.getValue()).stream()
                .map(CommentJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByParentId(CommentId parentId) {
        return jpaRepository.findByParentId(parentId.getValue()).stream()
                .map(CommentJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(CommentId id) {
        jpaRepository.deleteById(id.getValue());
    }
}
