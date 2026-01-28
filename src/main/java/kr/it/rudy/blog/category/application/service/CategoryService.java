package kr.it.rudy.blog.category.application.service;

import kr.it.rudy.blog.category.application.dto.CategoryResponse;
import kr.it.rudy.blog.category.application.dto.CreateCategoryRequest;
import kr.it.rudy.blog.category.application.dto.UpdateCategoryRequest;
import kr.it.rudy.blog.category.domain.Category;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.category.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("Category with slug '" + request.slug() + "' already exists");
        }

        Category category = Category.create(
                request.name(),
                request.slug(),
                request.description()
        );
        Category saved = categoryRepository.save(category);
        return CategoryResponse.from(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(String id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(CategoryId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));

        // slug 중복 체크 (자기 자신 제외)
        categoryRepository.findBySlug(request.slug())
                .filter(existing -> !existing.getId().equals(category.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Category with slug '" + request.slug() + "' already exists");
                });

        category.updateName(request.name());
        category.updateSlug(request.slug());
        category.updateDescription(request.description());

        Category updated = categoryRepository.save(category);
        return CategoryResponse.from(updated);
    }

    @Transactional
    public void deleteCategory(String id) {
        CategoryId categoryId = CategoryId.of(id);
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found: " + id);
        }
        categoryRepository.delete(categoryId);
    }

    public CategoryResponse getCategory(String id) {
        Category category = categoryRepository.findById(CategoryId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
        return CategoryResponse.from(category);
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with slug: " + slug));
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }
}
