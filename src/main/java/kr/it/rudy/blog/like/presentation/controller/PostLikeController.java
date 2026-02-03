package kr.it.rudy.blog.like.presentation.controller;

import kr.it.rudy.blog.like.application.dto.PostLikeResponse;
import kr.it.rudy.blog.like.application.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<PostLikeResponse> toggleLike(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PostLikeResponse response = postLikeService.toggleLike(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PostLikeResponse> getLikeStatus(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt != null ? jwt.getSubject() : null;
        PostLikeResponse response = postLikeService.getLikeStatus(postId, userId);
        return ResponseEntity.ok(response);
    }
}
