package com.ecommerce.controller;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.payload.request.create.CreateProductRequest;
import com.ecommerce.payload.request.update.UpdateProductRequest;
import com.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    private final ProductService productService;
    //TODO: return ProductDto or Product?
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long productId) {
        ProductDto product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequest createRequest) {
        Long savedProductId = productService.createProduct(createRequest);
        return new ResponseEntity<>(savedProductId, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long productId,
                                        @RequestBody UpdateProductRequest updateRequest) {
        productService.updateProduct(productId, updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product successfully deleted.", HttpStatus.OK);
    }
}
