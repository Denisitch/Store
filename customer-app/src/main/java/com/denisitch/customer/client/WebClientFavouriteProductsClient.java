package com.denisitch.customer.client;

import com.denisitch.customer.entity.FavouriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class WebClientFavouriteProductsClient implements FavouriteProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return null;
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        return null;
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId) {
        return null;
    }

    @Override
    public Mono<Void> removeProductFromFavourites(int productId) {
        return null;
    }
}
