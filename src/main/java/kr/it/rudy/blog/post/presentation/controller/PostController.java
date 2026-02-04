package kr.it.rudy.blog.post.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.blog.post.application.dto.CreatePostRequest;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import kr.it.rudy.blog.post.application.dto.UpdatePostRequest;
import kr.it.rudy.blog.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Post REST Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody CreatePostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String id) {
        PostResponse response = postService.getPost(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> searchPosts(
            @ModelAttribute SearchPostRequest request,
            Pageable pageable
    ) {
        Page<PostResponse> responses = postService.searchPosts(request, pageable);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable String id,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        PostResponse response = postService.updatePost(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishPost(@PathVariable String id) {
        postService.publishPost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<Void> unpublishPost(@PathVariable String id) {
        postService.unpublishPost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
