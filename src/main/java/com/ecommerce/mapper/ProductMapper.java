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
                product.getAppUser().getId(),
                product.getTitle(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getState(),
                product.getPrice()
        );
    }

    public static Product map(final CreateProductRequest createRequest, final AppUser appUser, final Category category){
        return new Product(
                appUser,
                createRequest.title(),
                createRequest.price(),
                createRequest.description(),
                category,
                ProductState.valueOf(createRequest.state())
        );
    }
}
