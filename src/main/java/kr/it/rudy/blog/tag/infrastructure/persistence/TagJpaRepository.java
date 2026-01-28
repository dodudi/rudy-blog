package kr.it.rudy.blog.tag.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagJpaRepository extends JpaRepository<TagJpaEntity, String> {

    Optional<TagJpaEntity> findBySlug(String slug);

    List<TagJpaEntity> findByIdIn(Set<String> ids);

    boolean existsBySlug(String slug);
}
