package com.pricecomparison.service;

import com.pricecomparison.dto.CategoryDto;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.CategoryMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.payload.request.CategoryRequest;
import com.pricecomparison.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private CategoryService underTest;
    private Category parentCategory;
    private Category category;
    private CategoryRequest request;
    private Long id;
    @BeforeEach
    void setUp() {
        underTest = new CategoryService(categoryRepository, categoryMapper);
        id = 1L;
        parentCategory = new Category(id, "Electronics", null);
        category = new Category(2L, "Smartphones", parentCategory);
        request = new CategoryRequest("Smartphones", parentCategory.getId());
    }
    @Test
    void canGetAllCategories() {
        //when
        underTest.getAllCategories();
        //then
        verify(categoryRepository).findAll();
    }

    @Test
    void canGetCategoryById() {
        //given
        given(categoryRepository.findById(id)).willReturn(Optional.of(category));
        CategoryDto expected = categoryMapper.map(category, parentCategory.getId());

        //when
        CategoryDto actual = underTest.getCategoryById(id);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetCategoryByIdReturnsEmptyOptional() {
        //given
        given(categoryRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category with id [%s] not found".formatted(id));
    }

    @Test
    void canCreateCategory() {
        //given
        given(categoryRepository.save(any(Category.class))).willReturn(category);
        given(categoryRepository.findById(id)).willReturn(Optional.of(parentCategory));

        //when
        Long returnedId = underTest.createCategory(request);

        //then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory.getId()).isNull();
        assertThat(capturedCategory.getTitle()).isEqualTo(request.title());
        assertThat(capturedCategory.getParent().getId()).isEqualTo(request.parentId());

        assertThat(returnedId).isEqualTo(category.getId());
    }

    @Test
    void willThrowWhenTryingToCreateParentCategoryNotFound() {
        //when
        //then
        assertThatThrownBy(() -> underTest.createCategory(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category with id [%s] not found".formatted(id));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void canUpdateCategory() {
        //given
        CategoryRequest updateRequest = new CategoryRequest("Laptops", parentCategory.getId());
        given(categoryRepository.findById(id)).willReturn(Optional.of(category));

        //when
        underTest.updateCategory(id, updateRequest);

        //then
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryArgumentCaptor.capture());
        Category capturedCategory = categoryArgumentCaptor.getValue();

        assertThat(capturedCategory.getTitle()).isEqualTo(updateRequest.title());
        assertThat(capturedCategory.getParent().getId()).isEqualTo(updateRequest.parentId());
    }

    @Test
    void willThrowWhenTryingToUpdateCategoryNotFound() {
        //when
        //then
        assertThatThrownBy(() -> underTest.updateCategory(id, any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category with id [%s] not found".formatted(id));

        verify(categoryRepository, never()).save(any());
    }

    @Test
    void canDeleteCategory() {
        //when
        underTest.deleteCategory(id);

        //then
        verify(categoryRepository).deleteById(id);
    }
}