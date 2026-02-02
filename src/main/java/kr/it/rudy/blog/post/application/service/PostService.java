package kr.it.rudy.blog.post.application.service;

import kr.it.rudy.blog.category.application.dto.CategoryResponse;
import kr.it.rudy.blog.category.domain.Category;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.category.domain.CategoryRepository;
import kr.it.rudy.blog.post.application.dto.CreatePostRequest;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.UpdatePostRequest;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import kr.it.rudy.blog.post.domain.PostStatus;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.domain.Tag;
import kr.it.rudy.blog.tag.domain.TagId;
import kr.it.rudy.blog.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional
    public PostResponse createPost(CreatePostRequest request) {
        CategoryId categoryId = null;
        if (request.categoryId() != null && !request.categoryId().isBlank()) {
            categoryId = CategoryId.of(request.categoryId());
            if (!categoryRepository.existsById(categoryId)) {
                throw new IllegalArgumentException("Category not found: " + request.categoryId());
            }
        }

        Set<TagId> tagIds = new HashSet<>();
        if (request.tagIds() != null && !request.tagIds().isEmpty()) {
            tagIds = request.tagIds().stream()
                    .map(TagId::of)
                    .collect(Collectors.toSet());
        }

        Post post = Post.create(
                request.title(),
                request.content(),
                request.summary(),
                request.author(),
                categoryId,
                tagIds
        );
        Post saved = postRepository.save(post);
        return toPostResponse(saved);
    }

    @Transactional
    public PostResponse updatePost(String id, UpdatePostRequest request) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.updateTitle(request.title());
        post.updateContent(request.content());
        post.updateSummary(request.summary());

        if (request.categoryId() != null && !request.categoryId().isBlank()) {
            CategoryId categoryId = CategoryId.of(request.categoryId());
            if (!categoryRepository.existsById(categoryId)) {
                throw new IllegalArgumentException("Category not found: " + request.categoryId());
            }
            post.updateCategory(categoryId);
        } else {
            post.updateCategory(null);
        }

        if (request.tagIds() != null) {
            Set<TagId> tagIds = request.tagIds().stream()
                    .map(TagId::of)
                    .collect(Collectors.toSet());
            post.updateTags(tagIds);
        }

        Post updated = postRepository.update(post);
        return toPostResponse(updated);
    }

    @Transactional
    public void publishPost(String id) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.publish();
        postRepository.update(post);
    }

    @Transactional
    public void unpublishPost(String id) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.unpublish();
        postRepository.update(post);
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
        return toPostResponse(post);
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll(Sort.by("title")).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByStatus(PostStatus status) {
        return postRepository.findByStatus(status).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByAuthor(String author) {
        return postRepository.findByAuthor(author).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByAuthorAndStatus(String author, PostStatus status) {
        return postRepository.findByAuthorAndStatus(author, status).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByCategoryId(String categoryId) {
        return postRepository.findByCategoryId(CategoryId.of(categoryId)).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByCategorySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with slug: " + slug));
        return postRepository.findByCategoryId(category.getId()).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByTagId(String tagId) {
        return postRepository.findByTagId(TagId.of(tagId)).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByTagSlug(String slug) {
        Tag tag = tagRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with slug: " + slug));
        return postRepository.findByTagId(tag.getId()).stream()
                .map(this::toPostResponse)
                .collect(Collectors.toList());
    }

    private PostResponse toPostResponse(Post post) {
        CategoryResponse categoryResponse = null;
        if (post.getCategoryId() != null) {
            categoryResponse = categoryRepository.findById(post.getCategoryId())
                    .map(CategoryResponse::from)
                    .orElse(null);
        }

        List<TagResponse> tagResponses = List.of();
        if (!post.getTagIds().isEmpty()) {
            tagResponses = tagRepository.findByIds(post.getTagIds()).stream()
                    .map(TagResponse::from)
                    .collect(Collectors.toList());
        }

        return PostResponse.from(post, categoryResponse, tagResponses);
    }
}
