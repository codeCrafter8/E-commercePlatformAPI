package com.pricecomparison.mapper;

import com.pricecomparison.dto.ProductDto;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import com.pricecomparison.payload.request.create.CreateProductRequest;

public class ProductMapper {
    public static ProductDto map(final Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getCategory().getId(),
                product.getEAN()
        );
    }

    public static Product map(final CreateProductRequest createRequest, final Category category){
        return new Product(
                createRequest.title(),
                category,
                createRequest.EAN()
        );
    }
}
