package kr.it.rudy.blog.tag.infrastructure.persistence;

import jakarta.persistence.*;
import kr.it.rudy.blog.common.infrastructure.persistence.BaseEntity;
import kr.it.rudy.blog.tag.domain.Tag;
import kr.it.rudy.blog.tag.domain.TagId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tags")
public class TagJpaEntity extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 50)
    private String slug;

    private TagJpaEntity(String id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }

    public static TagJpaEntity fromDomain(Tag tag) {
        return new TagJpaEntity(
                tag.getId().getValue(),
                tag.getName(),
                tag.getSlug()
        );
    }

    public Tag toDomain() {
        return Tag.reconstitute(
                TagId.of(this.id),
                this.name,
                this.slug,
                this.getCreatedDt(),
                this.getUpdatedDt()
        );
    }
}
