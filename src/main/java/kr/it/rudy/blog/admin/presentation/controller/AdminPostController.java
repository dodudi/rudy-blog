package kr.it.rudy.blog.admin.presentation.controller;

import kr.it.rudy.blog.admin.application.service.AdminPostService;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/posts")
public class AdminPostController {

    private final AdminPostService adminPostService;

    @GetMapping
    public ResponseEntity<Page<PostResponse>> listPosts(
            @ModelAttribute SearchPostRequest request,
            Pageable pageable
    ) {
        return ResponseEntity.ok(adminPostService.searchPosts(request, pageable));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishPost(@PathVariable String id) {
        adminPostService.publishPost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Void> unpublishPost(@PathVariable String id) {
        adminPostService.unpublishPost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        adminPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
