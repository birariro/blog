package com.birairo.blog.member.service.support;

import com.birairo.blog.article.service.ArticleValidator;
import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.member.domain.Comment;
import com.birairo.blog.member.domain.Member;
import com.birairo.blog.member.service.ArticleComments;
import com.birairo.blog.member.service.CommentCreator;
import com.birairo.blog.member.service.CommentLoader;
import com.birairo.blog.member.service.CommentModifier;
import com.birairo.blog.member.service.NestedComment;
import com.birairo.blog.member.service.ParentComment;
import com.birairo.blog.vo.Author;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Parent;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void createArticleComment(UUID articleId, Author author, Content content) {

        if (doNotExistArticle(articleId)) {
            throw new NoSuchEntityException("not found article");
        }
        Member member = memberRepository.findById(author.getValue())
                .orElseThrow();
        Comment comment = Comment.ofArticleComment(articleId, member, content);
        commentRepository.save(comment);
    }

    @Override
    public void createCommentToComment(UUID commentId, Author author, Content content) {

        if (doNotExistComment(commentId)) {
            throw new NoSuchEntityException("not found article");
        }
        Member member = memberRepository.findById(author.getValue())
                .orElseThrow();
        Comment comment = Comment.ofCommentToComment(commentId, member, content);
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

        Member member = memberRepository.findById(author.getValue())
                .orElseThrow();
        Comment comment = findComment(id);
        comment.modify(member, content);
    }

    /**
     * 게시글에 대한 댓글과
     * 댓글에 대한 대댓글을 조회한다.
     *
     * @param parentId
     * @return
     */
    public ArticleComments findParentComments(UUID parentId) {

        List<Comment> comments = commentRepository.findByParentIdWithAuthor(Parent.of(parentId));

        ArticleComments articleComments = new ArticleComments(parentId);

        for (Comment comment : comments) {

            ParentComment parentComment = new ParentComment(comment);
            List<Comment> nestedComments = commentRepository.findByParentIdWithAuthor(Parent.of(comment.getId()));

            for (Comment nestedComment : nestedComments) {
                parentComment.addNestedComment(new NestedComment(nestedComment));
            }
            articleComments.addParentComment(parentComment);
        }

        return articleComments;
    }

    @Override
    public Comment findComment(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("not found comment"));
    }
}
