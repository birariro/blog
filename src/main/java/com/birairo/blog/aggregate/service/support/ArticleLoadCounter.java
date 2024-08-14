package com.birairo.blog.aggregate.service.support;

import com.birairo.blog.aggregate.domain.Counter;
import com.birairo.blog.aggregate.service.ArticleLoadCount;
import com.birairo.blog.vo.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ArticleLoadCounter implements ArticleLoadCount {
    private final CounterRepository counterRepository;

    @Override
    public long loadArticleCount(UUID id) {

        Optional<Counter> counter = counterRepository.findFirstByParentId(Parent.of(id));
        if (counter.isEmpty()) {
            return 0L;
        }
        return counter.get().getCount();
    }
}
