package com.denisitch.catalogue.controller;

import com.denisitch.catalogue.controller.payload.NewProductPayload;
import com.denisitch.catalogue.entity.Product;
import com.denisitch.catalogue.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("catalogue-api/products")
@RequiredArgsConstructor
public class ProductsRestController {

    private final ProductService productService;

    private final MessageSource messageSource;

    @GetMapping
    public List<Product> getProducts() {
        return this.productService.findAllProducts();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody NewProductPayload payload,
                                                 BindingResult bindingResult,
                                                 UriComponentsBuilder uriBuilder,
                                                 Locale locale) {
        if (bindingResult.hasErrors()) {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            this.messageSource.getMessage("errors.403.title", new Object[]{},
                                    "errors.403.title", locale));
            problemDetail.setProperty("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest()
                    .body(problemDetail);
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                    .created(uriBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }
}
