package com.denisitch.feedback.controller;

import com.denisitch.feedback.entity.ProductReview;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Slf4j
@SpringBootTest
@AutoConfigureWebTestClient
class ProductReviewsRestControllerIT {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    void setUp() {
        this.reactiveMongoTemplate.insertAll(List.of(
                        new ProductReview(UUID.fromString("fe87eef6-cbd7-11ee-aeb6-275dac91de02"),
                                1, 1, "Отзыв № 1", "user-1"),
                        new ProductReview(UUID.fromString("37b79df0-cbda-11ee-b5d0-17231cdeab05"),
                                1, 3, "Отзыв № 2", "user-2"),
                        new ProductReview(UUID.fromString("23ff1d58-cbd8-11ee-9f4f-ef497a4e4799"),
                                1, 5, "Отзыв № 3", "user-3")
                ))
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        this.reactiveMongoTemplate.remove(ProductReview.class)
                .all()
                .block();
    }

    @Test
    void findProductReviewsByProductId_ReturnsReviews() {
        this.webTestClient
                .mutateWith(mockJwt())
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info("========== REQUEST ==========");
                    log.info("{} {}", clientRequest.method(), clientRequest.url());
                    clientRequest.headers().forEach((header, value) -> log.info("{}: {}", header, value));
                    log.info("======== END REQUEST ========");
                    return Mono.just(clientRequest);
                }))
                .build()
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        [
                          {"id": "fe87eef6-cbd7-11ee-aeb6-275dac91de02", "productId": 1, "rating": 1, 
                        "review": "Отзыв № 1", "userId": "user-1"},
                          {"id": "37b79df0-cbda-11ee-b5d0-17231cdeab05", "productId": 1, "rating": 3, 
                        "review": "Отзыв № 2", "userId": "user-2"},
                          {"id": "23ff1d58-cbd8-11ee-9f4f-ef497a4e4799", "productId": 1, "rating": 5, 
                        "review": "Отзыв № 3", "userId": "user-3"}
                        ]
                        """);
    }

    @Test
    void findProductReviewsByProductId_UserIsNotAuthenticated_ReturnsNotAuthorized() {
        this.webTestClient
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/1")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void createProductReview_RequestIsValid_ReturnsProductReview() {
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "productId": 1,
                        "rating": 5,
                        "review": "Эта пять"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        {
                        "productId": 1,
                        "rating": 5,
                        "review": "Эта пять",
                        "userId": "user-tester"
                        }
                        """)
                .jsonPath("$.id")
                .exists();
    }

    @Test
    void createProductReview_RequestIsInvalid_ReturnsBadRequest() {
        this.webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("user-tester")))
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                        "productId": null,
                        "rating": -1,
                        "review": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Sed euismod nisi porta lorem mollis aliquam ut. Enim nec dui nunc mattis enim ut tellus elementum. Quam adipiscing vitae proin sagittis nisl rhoncus mattis rhoncus urna. Amet volutpat consequat mauris nunc congue. Fermentum leo vel orci porta non pulvinar neque laoreet. Non tellus orci ac auctor augue mauris augue neque. Praesent elementum facilisis leo vel fringilla. At risus viverra adipiscing at in tellus integer feugiat scelerisque. Ipsum consequat nisl vel pretium lectus. Sit amet dictum sit amet justo donec enim diam. Rutrum tellus pellentesque eu tincidunt tortor aliquam. Tempus imperdiet nulla malesuada pellentesque. Urna nunc id cursus metus aliquam eleifend mi in nulla. Scelerisque viverra mauris in aliquam sem fringilla. Sed tempus urna et pharetra pharetra massa.                                           Tincidunt augue interdum velit euismod in pellentesque massa placerat. Erat velit scelerisque in dictum. Vestibulum sed arcu non odio euismod lacinia at. Suspendisse interdum consectetur libero id faucibus. Tristique et egestas quis ipsum suspendisse ultrices. Urna et pharetra pharetra massa. Ac turpis egestas integer eget. Ullamcorper morbi tincidunt ornare massa. Commodo quis imperdiet massa tincidunt nunc. Enim diam vulputate ut pharetra sit. Massa enim nec dui nunc mattis enim. Rhoncus aenean vel elit scelerisque mauris pellentesque pulvinar. Leo in vitae turpis massa sed. Id semper risus in hendrerit gravida. Orci ac auctor augue mauris augue neque gravida in fermentum. Viverra aliquet eget sit amet tellus cras adipiscing enim eu. Nunc consequat interdum varius sit amet. Ornare massa eget egestas purus viverra accumsan in nisl nisi. At consectetur lorem donec massa sapien faucibus. Enim ut sem viverra aliquet eget sit amet. Venenatis cras sed felis eget. In arcu cursus euismod quis. Adipiscing elit pellentesque habitant morbi tristique senectus et netus. Sit amet facilisis magna etiam. Id neque aliquam vestibulum morbi blandit cursus risus. Congue quisque egestas diam in arcu cursus euismod. Cras sed felis eget velit aliquet sagittis. A arcu cursus vitae congue mauris rhoncus aenean. At imperdiet dui accumsan sit amet nulla facilisi. Dictum varius duis at consectetur lorem donec massa sapien. Sit amet nisl suscipit adipiscing bibendum est. Elementum curabitur vitae nunc sed. Auctor eu augue ut lectus arcu bibendum. Morbi tristique senectus et netus et malesuada fames. Donec ac odio tempor orci dapibus. Diam vulputate ut pharetra sit amet aliquam. Cras sed felis eget velit aliquet sagittis. Ut faucibus pulvinar elementum integer enim neque volutpat ac tincidunt."
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().doesNotExist(HttpHeaders.LOCATION)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody()
                .json("""
                        {
                        "errors": [
                            "Товар не указан",
                            "Оценка меньше 1",
                            "Размер отзыва не должен превышать 1000 символов"
                          ]
                        }
                        """);
    }

    @Test
    void createProductReview_UserIsNotAuthenticated_ReturnsNotAuthorized() {
        // given

        // when
        this.webTestClient
                .post()
                .uri("/feedback-api/product-reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "productId": 1,
                            "rating": 5,
                            "review": "Эта пять"
                        }""")
                .exchange()
                // then
                .expectStatus().isUnauthorized();
    }
}