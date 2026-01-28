package kr.it.rudy.blog.category.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.category.domain.Category;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.common.infrastructure.persistence.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "categories")
public class CategoryJpaEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "description", length = 500)
    private String description;

    private CategoryJpaEntity(String id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
    }

    public static CategoryJpaEntity fromDomain(Category category) {
        return new CategoryJpaEntity(
                category.getId().getValue(),
                category.getName(),
                category.getSlug(),
                category.getDescription()
        );
    }

    public Category toDomain() {
        return Category.reconstitute(
                CategoryId.of(this.id),
                this.name,
                this.slug,
                this.description,
                this.getCreatedDt(),
                this.getUpdatedDt()
        );
    }
}
