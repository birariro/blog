package com.birairo.blog.comment.service.support;

import com.birairo.blog.article.service.ArticleValidator;
import com.birairo.blog.comment.domain.Comment;
import com.birairo.blog.comment.service.ArticleComments;
import com.birairo.blog.comment.service.CommentCreator;
import com.birairo.blog.comment.service.CommentLoader;
import com.birairo.blog.comment.service.CommentModifier;
import com.birairo.blog.comment.service.support.repository.CommentRepository;
import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService implements CommentCreator, CommentModifier, CommentLoader {
    private final CommentRepository commentRepository;
    private final ArticleValidator articleValidator;

    @Transactional
    public void createArticleComment(UUID articleId, Author author, Content content) {

        if (doNotExistArticle(articleId)) {
            throw new NoSuchEntityException("not found article");
        }

        Comment comment = Comment.ofArticleComment(articleId, author, content);
        commentRepository.save(comment);
    }

    @Override
    public void createCommentToComment(UUID commentId, Author author, Content content) {

        if (doNotExistComment(commentId)) {
            throw new NoSuchEntityException("not found article");
        }
        Comment comment = Comment.ofCommentToComment(commentId, author, content);
        commentRepository.save(comment);
    }

    private boolean doNotExistArticle(UUID articleId) {
        return !articleValidator.existsArticle(articleId);
    }

    private boolean doNotExistComment(UUID commentId) {
        return !commentRepository.existsById(commentId);
    }

    @Transactional
    public void modifyComment(UUID id, Author author, Content content) {

        Comment comment = findComment(id);
        comment.modify(author, content);
    }

    /**
     * 게시글에 대한 댓글과
     * 댓글에 대한 대댓글을 조회한다.
     *
     * @param parentId
     * @return
     */
    public ArticleComments findParentComments(UUID parentId) {

        List<Comment> comments = commentRepository.findByParentId(parentId);

        ArticleComments articleComments = new ArticleComments(parentId, comments);
        for (Comment comment : comments) {
            UUID commentId = comment.getId();
            articleComments.setNestedComments(commentId, commentRepository.findByParentId(commentId));
        }

        return articleComments;
    }

    @Override
    public Comment findComment(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("not found comment"));
    }
}