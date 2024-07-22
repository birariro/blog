package com.birairo.blog.article.controller;

import java.util.List;

record ArticleRequest(String title, String content, List<String> tags) {
}
