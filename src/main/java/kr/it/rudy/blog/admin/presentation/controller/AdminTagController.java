package kr.it.rudy.blog.admin.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.blog.tag.application.dto.CreateTagRequest;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.application.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/tags")
public class AdminTagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> listTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody CreateTagRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable String id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
