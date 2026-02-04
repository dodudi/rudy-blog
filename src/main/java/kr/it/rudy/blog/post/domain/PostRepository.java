package kr.it.rudy.blog.post.domain;

import kr.it.rudy.blog.post.infrastructure.persistence.PostJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Post update(Post post);

    Optional<Post> findById(PostId id);

    Page<Post> findAll(Specification<PostJpaEntity> spec, Pageable pageable);

    void delete(PostId id);

    boolean existsById(PostId id);
}
