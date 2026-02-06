package kr.it.rudy.blog.comment.domain;

import kr.it.rudy.blog.post.domain.PostId;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Comment update(Comment comment);

    Optional<Comment> findById(CommentId id);

    List<Comment> findByPostId(PostId postId);

    List<Comment> findByParentId(CommentId parentId);

    void delete(CommentId id);
}
