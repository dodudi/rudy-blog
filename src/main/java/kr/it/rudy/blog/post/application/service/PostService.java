package kr.it.rudy.blog.post.application.service;

import kr.it.rudy.blog.post.application.dto.CreatePostRequest;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.UpdatePostRequest;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import kr.it.rudy.blog.post.domain.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        Post post = Post.create(
                request.title(),
                request.content(),
                request.author()
        );
        Post saved = postRepository.save(post);
        return PostResponse.from(saved);
    }

    @Transactional
    public PostResponse updatePost(String id, UpdatePostRequest request) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.updateTitle(request.title());
        post.updateContent(request.content());

        Post updated = postRepository.save(post);
        return PostResponse.from(updated);
    }

    @Transactional
    public void publishPost(String id) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.publish();
        postRepository.save(post);
    }

    @Transactional
    public void unpublishPost(String id) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.unpublish();
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(String id) {
        PostId postId = PostId.of(id);
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found: " + id);
        }
        postRepository.delete(postId);
    }

    public PostResponse getPost(String id) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
        return PostResponse.from(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByStatus(PostStatus status) {
        return postRepository.findByStatus(status).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author).stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }
}
