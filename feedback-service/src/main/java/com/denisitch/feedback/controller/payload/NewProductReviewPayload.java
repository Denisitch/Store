package com.denisitch.feedback.controller.payload;

public record NewProductReviewPayload(
        Integer productId,
        Integer rating,
        String reviews
) {
}
