package kr.it.rudy.blog.post.infrastructure.persistence;

import kr.it.rudy.blog.post.domain.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, String> {

    List<PostJpaEntity> findByStatus(PostStatus status);

    List<PostJpaEntity> findByAuthor(String author);

    List<PostJpaEntity> findByAuthorAndStatus(String author, PostStatus status);

    List<PostJpaEntity> findByCategoryId(String categoryId);

    @Query("SELECT p FROM PostJpaEntity p WHERE :tagId MEMBER OF p.tagIds")
    List<PostJpaEntity> findByTagId(@Param("tagId") String tagId);
}
