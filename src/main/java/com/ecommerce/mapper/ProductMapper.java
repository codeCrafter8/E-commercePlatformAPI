package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.enumeration.AppUserRole;
import com.ecommerce.enumeration.ProductState;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.request.create.CreateAppUserRequest;
import com.ecommerce.payload.request.create.CreateProductRequest;

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
