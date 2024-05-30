package com.denisitch.customer.controller;

import com.denisitch.customer.client.FavouriteProductsClient;
import com.denisitch.customer.client.ProductReviewsClient;
import com.denisitch.customer.client.ProductsClient;
import com.denisitch.customer.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void loadProduct_ProductExist_ReturnsNotEmptyMono() {
        // given
        var product = new Product(1, "Товар № 1", "Описание товара № 1");
        doReturn(Mono.just(product)).when(this.productsClient).findProduct(1);
        // when
        StepVerifier.create(this.controller.loadProduct(1))
        // then
                .expectNext(product)
                .expectComplete()
                .verify();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favouriteProductsClient, this.productReviewsClient);
    }

    @Test
    void loadProduct_ProductDoesNotExist_ReturnsMonoWithNoSuchElementException() {
        // given
        doReturn(Mono.empty()).when(this.productsClient).findProduct(1);
        // when
        StepVerifier.create(this.controller.loadProduct(1))
        // then
                .expectErrorMatches(exception -> exception instanceof NoSuchElementException e &&
                        e.getMessage().equals("customer.products.error.not_found"))
                .verify();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favouriteProductsClient, this.productReviewsClient);
    }

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