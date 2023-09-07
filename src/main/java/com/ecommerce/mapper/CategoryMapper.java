package com.ecommerce.mapper;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.enumeration.ProductState;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.request.CategoryRequest;
import com.ecommerce.payload.request.create.CreateProductRequest;

public class CategoryMapper {
    public static Category map(final CategoryRequest createRequest){
        return new Category(
                createRequest.title()
        );
    }
    public static CategoryDto map(final Category category){
        return new CategoryDto(
                category.getId(),
                category.getTitle()
        );
    }
}
