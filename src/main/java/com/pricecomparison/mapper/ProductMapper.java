package com.pricecomparison.mapper;

import com.pricecomparison.dto.ProductDto;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import com.pricecomparison.payload.request.create.CreateProductRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductDto map(final Product product) {
        return new ProductDto(
                product.getId(),
                product.getTitle(),
                product.getCategory().getId(),
                product.getEAN()
        );
    }

    public Product map(final CreateProductRequest createRequest, final Category category) {
        return new Product(
                createRequest.title(),
                category,
                createRequest.EAN()
        );
    }

    public Product map(final ProductDto productDto, final Category category) {
        return new Product(
                productDto.title(),
                category,
                productDto.EAN()
        );
    }
}
