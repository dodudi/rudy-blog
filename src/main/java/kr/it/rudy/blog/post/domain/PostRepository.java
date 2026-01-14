package kr.it.rudy.blog.post.domain;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(PostId id);

    List<Post> findAll();

    List<Post> findByStatus(PostStatus status);

    List<Post> findByAuthor(String author);

    void delete(PostId id);

    boolean existsById(PostId id);
}
