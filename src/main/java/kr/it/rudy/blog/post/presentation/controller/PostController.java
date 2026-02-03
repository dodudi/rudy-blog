package kr.it.rudy.blog.post.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.blog.post.application.dto.CreatePostRequest;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import kr.it.rudy.blog.post.application.dto.UpdatePostRequest;
import kr.it.rudy.blog.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @ModelAttribute SearchPostRequest request
    ) {
        List<PostResponse> responses;
        if (request.categorySlug() != null && !request.categorySlug().isBlank()) {
            responses = postService.getPostsByCategorySlug(request.categorySlug());
        } else if (request.tagSlug() != null && !request.tagSlug().isBlank()) {
            responses = postService.getPostsByTagSlug(request.tagSlug());
        } else if (request.author() != null && !request.author().isBlank() && request.status() != null) {
            // author와 status 둘 다 있는 경우
            responses = postService.getPostsByAuthorAndStatus(request.author(), request.status());
        } else if (request.author() != null && !request.author().isBlank()) {
            responses = postService.getPostsByAuthor(request.author());
        } else if (request.status() != null) {
            responses = postService.getPostsByStatus(request.status());
        } else {
            responses = postService.getAllPosts();
        }
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
