package com.denisitch.customer.controller;

import com.denisitch.customer.client.FavouriteProductsClient;
import com.denisitch.customer.client.ProductReviewsClient;
import com.denisitch.customer.client.ProductsClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductsClient productsClient;

    @Mock
    FavouriteProductsClient favouriteProductsClient;

    @Mock
    ProductReviewsClient productReviewsClient;

    @InjectMocks
    ProductController controller;

    @Test
    @DisplayName("Exception NoSuchElementException will be translated to page errors/404")
    void handleNoSuchElementException_ReturnsError404() {
        var exception = new NoSuchElementException("Товар не найден");
        var model = new ConcurrentModel();

        var result = controller.handleNoSuchElementException(exception, model);

        assertEquals("errors/404", result);
        assertEquals("Товар не найден", model.getAttribute("error"));
    }
}