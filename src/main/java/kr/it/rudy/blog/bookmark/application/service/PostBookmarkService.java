package kr.it.rudy.blog.bookmark.application.service;

import kr.it.rudy.blog.bookmark.application.dto.PostBookmarkResponse;
import kr.it.rudy.blog.bookmark.domain.PostBookmark;
import kr.it.rudy.blog.bookmark.domain.PostBookmarkRepository;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostBookmarkService {

    private final PostBookmarkRepository postBookmarkRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostBookmarkResponse toggleBookmark(String postId, String userId) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        Optional<PostBookmark> existingBookmark = postBookmarkRepository.findByPostIdAndUserId(postIdObj, userId);

        boolean bookmarked;
        if (existingBookmark.isPresent()) {
            postBookmarkRepository.delete(existingBookmark.get().getId());
            bookmarked = false;
        } else {
            PostBookmark postBookmark = PostBookmark.create(postIdObj, userId);
            postBookmarkRepository.save(postBookmark);
            bookmarked = true;
        }

        return PostBookmarkResponse.of(postId, bookmarked);
    }

    public PostBookmarkResponse getBookmarkStatus(String postId, String userId) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        boolean bookmarked = postBookmarkRepository.existsByPostIdAndUserId(postIdObj, userId);
        return PostBookmarkResponse.of(postId, bookmarked);
    }

    public List<PostResponse> getBookmarkedPosts(String userId) {
        List<PostBookmark> bookmarks = postBookmarkRepository.findByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> postRepository.findById(bookmark.getPostId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }
}
