package com.denisitch.customer.controller;

import com.denisitch.customer.client.ProductsClient;
import com.denisitch.customer.entity.Product;
import com.denisitch.customer.payload.NewProductReviewPayload;
import com.denisitch.customer.service.FavouriteProductsService;
import com.denisitch.customer.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavouriteProductsService favouriteProductsService;

    private final ProductReviewsService productReviewsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId);
    }

    @GetMapping
    public Mono<String> getProductPage(
            @PathVariable("productId") int id,
            Model model
    ) {
        model.addAttribute("inFavourite", false);
        return this.productReviewsService.findProductReviewsByProduct(id)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouriteProductsService.findFavouriteProductByProduct(id)
                        .doOnNext(_ -> model.addAttribute("inFavourite", true)))
                .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductsService.addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("remove-from-favourites")
    public Mono<String> removeProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductsService.removeProductFromFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(
            @PathVariable("productId") int id,
            @Valid NewProductReviewPayload payload,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inFavourite", false);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return this.favouriteProductsService.findFavouriteProductByProduct(id)
                    .doOnNext(_ -> model.addAttribute("inFavourite", true))
                    .thenReturn("customer/products/product");
        } else {
            return this.productReviewsService.createProductReview(id, payload.rating(), payload.review())
                    .thenReturn("redirect:/customer/products/%d".formatted(id));
        }
    }
}
