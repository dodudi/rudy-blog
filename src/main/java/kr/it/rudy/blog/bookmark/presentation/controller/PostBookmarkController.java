package kr.it.rudy.blog.bookmark.presentation.controller;

import kr.it.rudy.blog.bookmark.application.dto.PostBookmarkResponse;
import kr.it.rudy.blog.bookmark.application.service.PostBookmarkService;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostBookmarkController {

    private final PostBookmarkService postBookmarkService;

    @PostMapping("/posts/{postId}/bookmarks")
    public ResponseEntity<PostBookmarkResponse> toggleBookmark(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PostBookmarkResponse response = postBookmarkService.toggleBookmark(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}/bookmarks")
    public ResponseEntity<PostBookmarkResponse> getBookmarkStatus(
            @PathVariable String postId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        PostBookmarkResponse response = postBookmarkService.getBookmarkStatus(postId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<List<PostResponse>> getBookmarkedPosts(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        List<PostResponse> responses = postBookmarkService.getBookmarkedPosts(userId);
        return ResponseEntity.ok(responses);
    }
}
