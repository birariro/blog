package com.birairo.blog.article.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagTest {

    @Test
    void should_tags_then_strings() {
        List<String> strings = List.of("tag1", "tag2", "tag3");

        List<Tag> tags = Tag.of(strings);
        assertThat(tags.stream().map(Tag::getName).toList())
                .isEqualTo(strings);
    }

    @Test
    void should_notDuplicateTags_then_duplicateStrings() {
        List<String> strings = List.of("tag1",
                "tag2", "tag2", //duplicate
                "tag3"
        );

        List<Tag> tags = Tag.of(strings);
        assertThat(tags.stream().map(Tag::getName).toList())
                .isEqualTo(List.of("tag1", "tag2", "tag3"));
    }
}