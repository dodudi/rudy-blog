package kr.it.rudy.blog.post.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, String>, JpaSpecificationExecutor<PostJpaEntity> {
}
