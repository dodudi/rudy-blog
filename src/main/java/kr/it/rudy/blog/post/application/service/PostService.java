package kr.it.rudy.blog.post.application.service;

import kr.it.rudy.blog.category.application.dto.CategoryResponse;
import kr.it.rudy.blog.category.domain.Category;
import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.category.domain.CategoryRepository;
import kr.it.rudy.blog.post.application.dto.CreatePostRequest;
import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import kr.it.rudy.blog.post.application.dto.UpdatePostRequest;
import kr.it.rudy.blog.post.domain.Post;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import kr.it.rudy.blog.post.domain.PostStatus;
import kr.it.rudy.blog.post.infrastructure.persistence.PostJpaEntity;
import kr.it.rudy.blog.post.infrastructure.persistence.PostSpecs;
import kr.it.rudy.blog.tag.application.dto.TagResponse;
import kr.it.rudy.blog.tag.domain.Tag;
import kr.it.rudy.blog.tag.domain.TagId;
import kr.it.rudy.blog.tag.domain.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public PostResponse createPost(CreatePostRequest request, String userId) {
        CategoryId categoryId = null;
        Category category = null;
        if (request.categoryId() != null && !request.categoryId().isBlank()) {
            categoryId = CategoryId.of(request.categoryId());
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.categoryId()));
        }

        // pinned=true일 때 카테고리 owner 검증
        if (Boolean.TRUE.equals(request.pinned())) {
            if (category == null) {
                throw new IllegalArgumentException("Cannot pin a post without a category");
            }
            if (!category.isOwner(userId)) {
                throw new SecurityException("Only the category owner can pin posts");
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

        // publish 플래그가 true이면 바로 게시
        if (Boolean.TRUE.equals(request.publish())) {
            post.publish();
        }

        // pinned 플래그 설정
        if (Boolean.TRUE.equals(request.pinned())) {
            post.updatePinned(true);
        }

        Post saved = postRepository.save(post);
        return toPostResponse(saved);
    }

    @Transactional
    public PostResponse updatePost(String id, UpdatePostRequest request, String userId) {
        Post post = postRepository.findById(PostId.of(id))
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        post.updateTitle(request.title());
        post.updateContent(request.content());
        post.updateSummary(request.summary());

        Category category = null;
        if (request.categoryId() != null && !request.categoryId().isBlank()) {
            CategoryId categoryId = CategoryId.of(request.categoryId());
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + request.categoryId()));
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

        // publish 플래그에 따라 상태 변경
        if (Boolean.TRUE.equals(request.publish()) && post.getStatus() == PostStatus.DRAFT) {
            post.publish();
        } else if (Boolean.FALSE.equals(request.publish()) && post.getStatus() == PostStatus.PUBLISHED) {
            post.unpublish();
        }

        // pinned 플래그 설정 - 변경 시 카테고리 owner 검증
        if (request.pinned() != null && request.pinned() != post.isPinned()) {
            if (request.pinned()) {
                if (category == null) {
                    throw new IllegalArgumentException("Cannot pin a post without a category");
                }
                if (!category.isOwner(userId)) {
                    throw new SecurityException("Only the category owner can pin posts");
                }
            }
            post.updatePinned(request.pinned());
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

    public Page<PostResponse> searchPosts(SearchPostRequest request, Pageable pageable) {
        SearchPostRequest resolvedRequest = resolveSlugToId(request);
        Specification<PostJpaEntity> spec = PostSpecs.searchWith(resolvedRequest);
        return postRepository.findAll(spec, pageable)
                .map(this::toPostResponse);
    }

    private SearchPostRequest resolveSlugToId(SearchPostRequest request) {
        SearchPostRequest result = request;

        if (request.categorySlug() != null && !request.categorySlug().isBlank() &&
            (request.categoryId() == null || request.categoryId().isBlank())) {
            Category category = categoryRepository.findBySlug(request.categorySlug())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with slug: " + request.categorySlug()));
            result = result.withCategoryId(category.getId().getValue());
        }

        if (request.tagSlug() != null && !request.tagSlug().isBlank() &&
            (request.tagId() == null || request.tagId().isBlank())) {
            Tag tag = tagRepository.findBySlug(request.tagSlug())
                    .orElseThrow(() -> new IllegalArgumentException("Tag not found with slug: " + request.tagSlug()));
            result = result.withTagId(tag.getId().getValue());
        }

        return result;
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
