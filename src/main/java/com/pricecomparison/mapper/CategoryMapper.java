package com.pricecomparison.mapper;

import com.pricecomparison.dto.CategoryDto;
import com.pricecomparison.model.Category;
import com.pricecomparison.payload.request.CategoryRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {
    public Category map(final CategoryRequest createRequest, final Category parent){
        return new Category(
                createRequest.title(),
                parent
        );
    }
    public CategoryDto map(final Category category, final Long parentId){
        return new CategoryDto(
                category.getId(),
                category.getTitle(),
                parentId
        );
    }
}
