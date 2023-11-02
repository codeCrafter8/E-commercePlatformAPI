package com.pricecomparison.service;

import com.pricecomparison.dto.CategoryDto;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.CategoryMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.payload.request.CategoryRequest;
import com.pricecomparison.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    Long parentId = category.getParent() == null ? -1 : category.getParent().getId();
                    return categoryMapper.map(category, parentId);
                })
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(categoryId))
                );

        Long parentId = category.getParent() == null ? -1 : category.getParent().getId();
        return categoryMapper.map(category, parentId);
    }

    public Long createCategory(CategoryRequest createRequest) {
        Category parent = null;
        if(createRequest.parentId() != null) {
            parent = categoryRepository.findById(createRequest.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category with id [%s] not found".formatted(createRequest.parentId()))
                    );
        }

        Category category = categoryMapper.map(createRequest, parent);
        category = categoryRepository.save(category);

        return category.getId();
    }

    public void updateCategory(Long categoryId, CategoryRequest updateRequest) {
        //TODO: what about parent id?

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(categoryId))
                );

        category.setTitle(updateRequest.title());
        category.setImageURL(updateRequest.imageUrl());

        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    //TODO: is better way?
    public Category getCategoryEntityById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(categoryId))
                );
    }
}
