package com.birairo.blog.comment;


import com.birairo.blog.env.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CommentAcceptanceTest extends AcceptanceTest {

    String commentContent = "게시글에 대한 댓글 입니다.";

    @Test
    void 댓글작성() {

        String targetId = UUID.randomUUID().toString();
        댓글_작성(targetId);
        댓글_조회(targetId);
    }

    private void 댓글_조회(String id) {

        String url = "/comment/target/" + id;
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> content = response.jsonPath().getList("content", String.class);
        Assertions.assertThat(content).containsExactly(commentContent);
    }

    private void 댓글_작성(String id) {

        String url = "/comment";
        Map<String, Object> body = Map.of(
                "target", id,
                "author", "commentAuthor",
                "content", commentContent
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().log().all().post(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}