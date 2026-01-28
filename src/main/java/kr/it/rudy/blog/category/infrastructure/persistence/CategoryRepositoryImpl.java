package kr.it.rudy.blog.category.infrastructure.persistence;

import kr.it.rudy.blog.category.domain.Category;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.category.domain.CategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;

    public CategoryRepositoryImpl(CategoryJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = CategoryJpaEntity.fromDomain(category);
        CategoryJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return jpaRepository.findById(id.getValue())
                .map(CategoryJpaEntity::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
                .map(CategoryJpaEntity::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream()
                .map(CategoryJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(CategoryId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(CategoryId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }
}
