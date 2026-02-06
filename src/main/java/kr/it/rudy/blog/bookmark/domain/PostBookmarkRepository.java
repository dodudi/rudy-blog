package kr.it.rudy.blog.bookmark.domain;

import kr.it.rudy.blog.post.domain.PostId;

import java.util.List;
import java.util.Optional;

public interface PostBookmarkRepository {
    PostBookmark save(PostBookmark postBookmark);

    Optional<PostBookmark> findByPostIdAndUserId(PostId postId, String userId);

    List<PostBookmark> findByUserId(String userId);

    void delete(PostBookmarkId id);

    boolean existsByPostIdAndUserId(PostId postId, String userId);
}
