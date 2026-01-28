package kr.it.rudy.blog.tag.infrastructure.persistence;

import kr.it.rudy.blog.tag.domain.Tag;
import kr.it.rudy.blog.tag.domain.TagId;
import kr.it.rudy.blog.tag.domain.TagRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final TagJpaRepository jpaRepository;

    public TagRepositoryImpl(TagJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tag save(Tag tag) {
        TagJpaEntity entity = TagJpaEntity.fromDomain(tag);
        TagJpaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Tag> findById(TagId id) {
        return jpaRepository.findById(id.getValue())
                .map(TagJpaEntity::toDomain);
    }

    @Override
    public Optional<Tag> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
                .map(TagJpaEntity::toDomain);
    }

    @Override
    public List<Tag> findAll() {
        return jpaRepository.findAll().stream()
                .map(TagJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> findByIds(Set<TagId> ids) {
        Set<String> idValues = ids.stream()
                .map(TagId::getValue)
                .collect(Collectors.toSet());
        return jpaRepository.findByIdIn(idValues).stream()
                .map(TagJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(TagId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(TagId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }
}
