package com.denisitch.customer.client;

import com.denisitch.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientProductReviewsClient implements ProductReviewsClient {
    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return null;
    }

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String reviews) {
        return null;
    }
}
