package kr.it.rudy.blog.category.domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(CategoryId id);

    Optional<Category> findBySlug(String slug);

    List<Category> findAll();

    void delete(CategoryId id);

    boolean existsById(CategoryId id);

    boolean existsBySlug(String slug);
}
