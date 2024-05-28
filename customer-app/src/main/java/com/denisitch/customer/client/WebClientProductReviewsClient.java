package com.denisitch.customer.client;

import com.denisitch.customer.entity.ProductReview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebClientProductReviewsClient implements ProductReviewsClient {

    private final WebClient webClient;

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return null;
    }

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String reviews) {
        return null;
    }
}
