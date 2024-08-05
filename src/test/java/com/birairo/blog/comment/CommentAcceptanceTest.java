package com.birairo.blog.comment;


import com.birairo.blog.env.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CommentAcceptanceTest extends AcceptanceTest {

    String commentContent = "게시글에 대한 댓글 입니다.";
    String commentToCommentContent = "댓글 대한 댓글 입니다.";

    @Test
    void 댓글작성() {

        게시글_작성();
        String targetId = 게시글_전체_조회후_id_조회();
        게시글_댓글_작성(targetId);
        String commentId = 게시글_댓글_조회_후_id조회(targetId);
        게시글_댓글에_댓글_작성(commentId);
        게시글_댓글_조회(targetId);
    }

    private String 게시글_전체_조회후_id_조회() {
        String url = "/article";
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        JsonPath jsonPath = response.jsonPath();


        List<String> id = jsonPath.getList("id", String.class);
        return id.get(0);
    }

    private void 게시글_작성() {

        String url = "/article";

        Map<String, Object> body = Map.of(
                "title", "articleTitle",
                "content", "articleContent",
                "tags", List.of("articleTags")
        );

        ExtractableResponse<Response> response = RestAssured
                .given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().post(url)
                .then().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private String 게시글_댓글_조회_후_id조회(String id) {

        String url = "/article/" + id + "/comment";
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String content = response.jsonPath().getString("comments[0].content");
        Assertions.assertThat(content).isEqualTo(commentContent);

        return response.jsonPath().getString("comments[0].id");
    }

    private void 게시글_댓글_조회(String id) {

        String url = "/article/" + id + "/comment";
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String content = response.jsonPath().getString("comments[0].comments[0].content");
        Assertions.assertThat(content).isEqualTo(commentToCommentContent);
    }

    private void 게시글_댓글_작성(String id) {

        String url = "/article/" + id + "/comment";
        Map<String, Object> body = Map.of(
                "author", "commentAuthor",
                "content", commentContent
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().log().all().post(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 게시글_댓글에_댓글_작성(String id) {

        String url = "/comment/" + id + "/comment";
        Map<String, Object> body = Map.of(
                "author", "commentCommentAuthor",
                "content", commentToCommentContent
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().log().all().post(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}