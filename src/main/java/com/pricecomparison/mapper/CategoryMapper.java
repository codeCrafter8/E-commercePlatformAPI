package com.pricecomparison.mapper;

import com.pricecomparison.dto.CategoryDto;
import com.pricecomparison.model.Category;
import com.pricecomparison.payload.request.CategoryRequest;

public class CategoryMapper {
    public static Category map(final CategoryRequest createRequest, final Category parent){
        return new Category(
                createRequest.title(),
                parent
        );
    }
    public static CategoryDto map(final Category category, final Long parentId){
        return new CategoryDto(
                category.getId(),
                category.getTitle(),
                parentId
        );
    }
}
