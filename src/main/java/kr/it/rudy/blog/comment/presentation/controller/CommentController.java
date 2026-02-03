package kr.it.rudy.blog.comment.presentation.controller;

import jakarta.validation.Valid;
import kr.it.rudy.blog.comment.application.dto.CommentResponse;
import kr.it.rudy.blog.comment.application.dto.CreateCommentRequest;
import kr.it.rudy.blog.comment.application.dto.UpdateCommentRequest;
import kr.it.rudy.blog.comment.application.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable String postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CommentResponse response = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String postId) {
        List<CommentResponse> responses = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String currentUser = jwt.getSubject();
        CommentResponse response = commentService.updateComment(postId, commentId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String currentUser = jwt.getSubject();
        commentService.deleteComment(postId, commentId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
