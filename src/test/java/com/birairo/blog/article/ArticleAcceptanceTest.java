package com.birairo.blog.article;


import com.birairo.blog.env.AcceptanceTest;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleAcceptanceTest extends AcceptanceTest {

    String articleTitle = "게시글 제목 입니다.";
    String articleContent = "게시글 내용 입니다.";
    List<String> articleTags = List.of("AcceptanceTag1", "AcceptanceTag2");

    @Test
    @WithMockUser
    void 게시글_작성_변경_조회() {

        게시글_작성();
        String id = 게시글_전체_조회후_id_조회();

        String newArticleTitle = "새로운 게시글 제목";
        String newArticleContent = "새로운 게시글 내용";
        게시글_수정(id, newArticleTitle, newArticleContent);
        게시글을_조회하면_변경한_내용이_보인다(id, newArticleTitle, newArticleContent);
    }

    private void 게시글을_조회하면_변경한_내용이_보인다(String id, String title, String content) {

        JsonPath 게시글_조회 = 게시글_조회(id);
        String responseTitle = 게시글_조회.getString("title");
        String responseContent = 게시글_조회.getString("content");
        Assertions.assertThat(responseTitle).isEqualTo(title);
        Assertions.assertThat(responseContent).isEqualTo(content);
    }

    private void 게시글_수정(String id, String title, String content) {

        String url = "/article/" + id;

        Map<String, Object> body = Map.of(
                "title", title,
                "content", content,
                "tags", articleTags
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().log().all().put(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private JsonPath 게시글_조회(String id) {
        String url = "/article/" + id;
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        return response.jsonPath();
    }


    private String 게시글_전체_조회후_id_조회() {
        String url = "/article";
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all().get(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        JsonPath jsonPath = response.jsonPath();

        List<String> titles = jsonPath.getList("title", String.class);

        Assertions.assertThat(titles).contains(articleTitle);
        List<String> id = jsonPath.getList("id", String.class);
        return id.get(0);
    }

    private void 게시글_작성() {

        String url = "/article";

        Map<String, Object> body = Map.of(
                "title", articleTitle,
                "content", articleContent,
                "tags", articleTags
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
                .when().log().all().post(url)
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


}