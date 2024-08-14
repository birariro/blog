package com.birairo.blog.aggregate.service.support;

import com.birairo.blog.aggregate.domain.Counter;
import com.birairo.blog.article.service.ArticleReadEvent;
import com.birairo.blog.article.service.GreetingEvent;
import com.birairo.blog.vo.Parent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CountEventHandler {

    private final CounterRepository counterRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(GreetingEvent event) {

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ArticleReadEvent event) {
        Parent parent = Parent.of(event.getArticle().getId());
        if (counterRepository.existsByParentId(parent)) {
            counterRepository.incrementCountByParentId(parent);

        } else {
            Counter counter = Counter.of(parent);
            counterRepository.save(counter);
        }

    }
}
