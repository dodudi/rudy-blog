package kr.it.rudy.blog.tag.application.service;

import kr.it.rudy.blog.tag.application.dto.CreateTagRequest;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.application.dto.UpdateTagRequest;
import kr.it.rudy.blog.tag.domain.Tag;
import kr.it.rudy.blog.tag.domain.TagId;
import kr.it.rudy.blog.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagResponse createTag(CreateTagRequest request) {
        if (tagRepository.existsBySlug(request.slug())) {
            throw new IllegalArgumentException("Tag with slug '" + request.slug() + "' already exists");
        }

        Tag tag = Tag.create(
                request.name(),
                request.slug()
        );
        Tag saved = tagRepository.save(tag);
        return TagResponse.from(saved);
    }

    @Transactional
    public TagResponse updateTag(String id, UpdateTagRequest request) {
        Tag tag = tagRepository.findById(TagId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + id));

        // slug 중복 체크 (자기 자신 제외)
        tagRepository.findBySlug(request.slug())
                .filter(existing -> !existing.getId().equals(tag.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Tag with slug '" + request.slug() + "' already exists");
                });

        tag.updateName(request.name());
        tag.updateSlug(request.slug());

        Tag updated = tagRepository.save(tag);
        return TagResponse.from(updated);
    }

    @Transactional
    public void deleteTag(String id) {
        TagId tagId = TagId.of(id);
        if (!tagRepository.existsById(tagId)) {
            throw new IllegalArgumentException("Tag not found: " + id);
        }
        tagRepository.delete(tagId);
    }

    public TagResponse getTag(String id) {
        Tag tag = tagRepository.findById(TagId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Tag not found: " + id));
        return TagResponse.from(tag);
    }

    public TagResponse getTagBySlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with slug: " + slug));
        return TagResponse.from(tag);
    }

    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    public List<TagResponse> getTagsByIds(Set<String> ids) {
        Set<TagId> tagIds = ids.stream()
                .map(TagId::of)
                .collect(Collectors.toSet());
        return tagRepository.findByIds(tagIds).stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }
}
