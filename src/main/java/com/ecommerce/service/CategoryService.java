package com.ecommerce.service;

import com.ecommerce.dto.CategoryDto;
import com.ecommerce.enumeration.ProductState;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.model.Category;
import com.ecommerce.payload.request.CategoryRequest;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> {
                    Long parentId = category.getParent() == null ? -1 : category.getParent().getId();
                    return CategoryMapper.map(category, parentId);
                })
                .collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(categoryId))
                );

        Long parentId = category.getParent() == null ? -1 : category.getParent().getId();
        return CategoryMapper.map(category, parentId);
    }

    public Long createCategory(CategoryRequest createRequest) {
        Category parent = null;
        if(createRequest.parentId() != null) {
            parent = categoryRepository.findById(createRequest.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category with id [%s] not found".formatted(createRequest.parentId()))
                    );
        }

        Category category = CategoryMapper.map(createRequest, parent);
        category = categoryRepository.save(category);

        return category.getId();
    }

    public void updateCategory(Long categoryId, CategoryRequest updateRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(categoryId))
                );

        category.setTitle(updateRequest.title());

        categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
