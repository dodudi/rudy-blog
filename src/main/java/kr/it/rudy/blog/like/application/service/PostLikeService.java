package kr.it.rudy.blog.like.application.service;

import kr.it.rudy.blog.like.application.dto.PostLikeResponse;
import kr.it.rudy.blog.like.domain.PostLike;
import kr.it.rudy.blog.like.domain.PostLikeRepository;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostLikeResponse toggleLike(String postId, String userId) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserId(postIdObj, userId);

        boolean liked;
        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get().getId());
            liked = false;
        } else {
            PostLike postLike = PostLike.create(postIdObj, userId);
            postLikeRepository.save(postLike);
            liked = true;
        }

        long likeCount = postLikeRepository.countByPostId(postIdObj);
        return PostLikeResponse.of(postId, likeCount, liked);
    }

    public PostLikeResponse getLikeStatus(String postId, String userId) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        long likeCount = postLikeRepository.countByPostId(postIdObj);
        boolean liked = userId != null && postLikeRepository.existsByPostIdAndUserId(postIdObj, userId);

        return PostLikeResponse.of(postId, likeCount, liked);
    }
}
