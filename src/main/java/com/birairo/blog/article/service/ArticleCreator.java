package com.birairo.blog.article.service;

import com.birairo.blog.article.domain.Tag;
import com.birairo.blog.vo.Content;
import com.birairo.blog.vo.Title;

import java.util.List;

public interface ArticleCreator {
    void createArticle(Title title, Content content, List<Tag> tags);
}
