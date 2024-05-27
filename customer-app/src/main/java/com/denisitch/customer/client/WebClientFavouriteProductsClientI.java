package com.denisitch.customer.client;

import com.denisitch.customer.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class WebClientFavouriteProductsClientI implements FavouriteProductsClient {
    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return null;
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProductByProductId(int productId) {
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
