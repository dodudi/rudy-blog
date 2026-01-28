package kr.it.rudy.blog.category.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, String> {

    Optional<CategoryJpaEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
