package com.birairo.blog.env;

import com.birairo.config.P6SpyConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(P6SpyConfig.class)
@DataJpaTest
@ActiveProfiles("h2")
//@ActiveProfiles("testcontainers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface DomainTest {
}
