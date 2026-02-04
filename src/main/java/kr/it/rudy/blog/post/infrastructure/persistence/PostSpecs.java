package kr.it.rudy.blog.post.infrastructure.persistence;

import jakarta.persistence.criteria.Predicate;
import kr.it.rudy.blog.post.application.dto.SearchPostRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PostSpecs {

    public static Specification<PostJpaEntity> searchWith(SearchPostRequest request) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.categoryId())) {
                predicates.add(builder.equal(root.get("categoryId"), request.categoryId()));
            }
            if (StringUtils.hasText(request.tagId())) {
                predicates.add(builder.isMember(request.tagId(), root.get("tagIds")));
            }
            if (StringUtils.hasText(request.author())) {
                predicates.add(builder.equal(root.get("author"), request.author()));
            }
            if (request.status() != null) {
                predicates.add(builder.equal(root.get("status"), request.status()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
