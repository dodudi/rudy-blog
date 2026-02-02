package kr.it.rudy.blog.post.domain;

import kr.it.rudy.blog.category.domain.CategoryId;
import kr.it.rudy.blog.tag.domain.TagId;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Post update(Post post);

    Optional<Post> findById(PostId id);

    List<Post> findAll(Sort title);

    List<Post> findByStatus(PostStatus status);

    List<Post> findByAuthor(String author);

    List<Post> findByAuthorAndStatus(String author, PostStatus status);

    List<Post> findByCategoryId(CategoryId categoryId);

    List<Post> findByTagId(TagId tagId);

    void delete(PostId id);

    boolean existsById(PostId id);
}
