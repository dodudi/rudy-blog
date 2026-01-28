package kr.it.rudy.blog.tag.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {
    Tag save(Tag tag);

    Optional<Tag> findById(TagId id);

    Optional<Tag> findBySlug(String slug);

    List<Tag> findAll();

    List<Tag> findByIds(Set<TagId> ids);

    void delete(TagId id);

    boolean existsById(TagId id);

    boolean existsBySlug(String slug);
}
