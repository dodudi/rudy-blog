package kr.it.rudy.blog.admin.application.service;

import kr.it.rudy.blog.post.application.dto.PostResponse;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import kr.it.rudy.blog.post.application.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPostService {

    private final PostService postService;

    public Page<PostResponse> searchPosts(SearchPostRequest request, Pageable pageable) {
        return postService.searchPosts(request, pageable);
    }

    @Transactional
    public void publishPost(String id) {
        postService.publishPost(id);
    }

    @Transactional
    public void unpublishPost(String id) {
        postService.unpublishPost(id);
    }

    @Transactional
    public void deletePost(String id) {
        postService.deletePost(id);
    }
}
