package com.denisitch.feedback.controller;

import com.denisitch.feedback.entity.ProductReview;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
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
    WebTestClient webTestClientClient;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Test
    void findProductReviewsByProductId_ReturnsReviews() {
        this.reactiveMongoTemplate.insertAll(List.of(
                        new ProductReview(UUID.fromString("fe87eef6-cbd7-11ee-aeb6-275dac91de02"),
                                1, 1, "Отзыв № 1", "user-1"),
                        new ProductReview(UUID.fromString("37b79df0-cbda-11ee-b5d0-17231cdeab05"),
                                1, 3, "Отзыв № 2", "user-2"),
                        new ProductReview(UUID.fromString("23ff1d58-cbd8-11ee-9f4f-ef497a4e4799"),
                                1, 5, "Отзыв № 3", "user-3")
                ))
                .blockLast();

        this.webTestClientClient.mutateWith(mockJwt())
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
                          {"id": "fe87eef6-cbd7-11ee-aeb6-275dac91de02", "productId": 1, "rating": 1, "review": "Отзыв № 1", "userId": "user-1"},
                          {"id": "37b79df0-cbda-11ee-b5d0-17231cdeab05", "productId": 1, "rating": 3, "review": "Отзыв № 2", "userId": "user-2"},
                          {"id": "23ff1d58-cbd8-11ee-9f4f-ef497a4e4799", "productId": 1, "rating": 5, "review": "Отзыв № 3", "userId": "user-3"}
                        ]
                        """);
    }
}