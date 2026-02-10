package kr.it.rudy.blog.admin.application.service;

import kr.it.rudy.blog.tag.application.dto.CreateTagRequest;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.application.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTagService {

    private final TagService tagService;

    public List<TagResponse> getAllTags() {
        return tagService.getAllTags();
    }

    @Transactional
    public TagResponse createTag(CreateTagRequest request) {
        return tagService.createTag(request);
    }

    @Transactional
    public void deleteTag(String id) {
        tagService.deleteTag(id);
    }
}
