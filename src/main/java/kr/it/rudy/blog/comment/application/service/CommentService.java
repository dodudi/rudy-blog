package kr.it.rudy.blog.comment.application.service;

import kr.it.rudy.blog.comment.application.dto.CommentResponse;
import kr.it.rudy.blog.comment.application.dto.CreateCommentRequest;
import kr.it.rudy.blog.comment.application.dto.UpdateCommentRequest;
import kr.it.rudy.blog.comment.domain.Comment;
import kr.it.rudy.blog.comment.domain.CommentId;
import kr.it.rudy.blog.comment.domain.CommentRepository;
import kr.it.rudy.blog.post.domain.PostId;
import kr.it.rudy.blog.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse createComment(String postId, CreateCommentRequest request) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        Comment comment;
        if (request.parentId() != null && !request.parentId().isBlank()) {
            CommentId parentIdObj = CommentId.of(request.parentId());
            Comment parentComment = commentRepository.findById(parentIdObj)
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found: " + request.parentId()));

            // 대대댓글 방지: 부모 댓글이 이미 대댓글인 경우 거부
            if (parentComment.isReply()) {
                throw new IllegalArgumentException("Cannot reply to a reply. Only one level of nesting is allowed.");
            }

            comment = Comment.createReply(postIdObj, parentIdObj, request.content(), request.author());
        } else {
            comment = Comment.create(postIdObj, request.content(), request.author());
        }

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);
    }

    @Transactional
    public CommentResponse updateComment(String postId, String commentId, UpdateCommentRequest request, String currentUser) {
        PostId postIdObj = PostId.of(postId);
        CommentId commentIdObj = CommentId.of(commentId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        Comment comment = commentRepository.findById(commentIdObj)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        // postId 일치 확인
        if (!comment.getPostId().equals(postIdObj)) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        // 작성자 확인
        if (!comment.isOwnedBy(currentUser)) {
            throw new IllegalStateException("You can only edit your own comments");
        }

        comment.updateContent(request.content());
        Comment updated = commentRepository.update(comment);
        return CommentResponse.from(updated);
    }

    @Transactional
    public void deleteComment(String postId, String commentId, String currentUser) {
        PostId postIdObj = PostId.of(postId);
        CommentId commentIdObj = CommentId.of(commentId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        Comment comment = commentRepository.findById(commentIdObj)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

        // postId 일치 확인
        if (!comment.getPostId().equals(postIdObj)) {
            throw new IllegalArgumentException("Comment does not belong to the specified post");
        }

        // 작성자 확인
        if (!comment.isOwnedBy(currentUser)) {
            throw new IllegalStateException("You can only delete your own comments");
        }

        // 대댓글도 함께 삭제 (DB CASCADE로 처리되지만 명시적으로)
        List<Comment> replies = commentRepository.findByParentId(commentIdObj);
        for (Comment reply : replies) {
            commentRepository.delete(reply.getId());
        }

        commentRepository.delete(commentIdObj);
    }

    public List<CommentResponse> getCommentsByPostId(String postId) {
        PostId postIdObj = PostId.of(postId);

        if (!postRepository.existsById(postIdObj)) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }

        List<Comment> allComments = commentRepository.findByPostId(postIdObj);

        // 부모 댓글과 대댓글 분리
        List<Comment> parentComments = allComments.stream()
                .filter(c -> !c.isReply())
                .collect(Collectors.toList());

        Map<String, List<Comment>> repliesByParentId = allComments.stream()
                .filter(Comment::isReply)
                .collect(Collectors.groupingBy(c -> c.getParentId().getValue()));

        // 계층 구조로 변환
        return parentComments.stream()
                .map(parent -> {
                    List<Comment> replies = repliesByParentId.getOrDefault(parent.getId().getValue(), new ArrayList<>());
                    List<CommentResponse> replyResponses = replies.stream()
                            .map(CommentResponse::from)
                            .collect(Collectors.toList());
                    return CommentResponse.from(parent, replyResponses);
                })
                .collect(Collectors.toList());
    }
}
