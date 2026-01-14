package kr.it.rudy.blog.post.infrastructure.persistence;

import kr.it.rudy.blog.post.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, String> {

    List<PostJpaEntity> findByStatus(PostStatus status);

    List<PostJpaEntity> findByAuthor(String author);
}
