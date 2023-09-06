package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.enumeration.AppUserRole;
import com.ecommerce.enumeration.ProductState;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Product;
import com.ecommerce.payload.request.create.CreateAppUserRequest;
import com.ecommerce.payload.request.create.CreateProductRequest;

public class ProductMapper {
    /*public static AppUserDto map(final AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail()
        );
    }*/

    public static Product map(final CreateProductRequest createRequest, AppUser appUser){
        return new Product(
                appUser,
                createRequest.title(),
                createRequest.price(),
                createRequest.description(),
                createRequest.category(),
                ProductState.valueOf(createRequest.state())
        );
    }
}
