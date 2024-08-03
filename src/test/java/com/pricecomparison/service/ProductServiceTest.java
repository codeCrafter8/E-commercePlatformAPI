package com.pricecomparison.service;

import com.pricecomparison.dto.ProductDto;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.ProductMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import com.pricecomparison.payload.request.create.CreateProductRequest;
import com.pricecomparison.payload.request.update.UpdateProductRequest;
import com.pricecomparison.repository.ProductRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;
    private final ProductMapper productMapper = new ProductMapper();
    private ProductService underTest;
    private Category category;
    private Product product;
    private CreateProductRequest createRequest;
    private Long id;

    @BeforeEach
    void setUp() {
        underTest = new ProductService(productRepository, categoryService, productMapper);
        id = 1L;
        category = new Category(id, "Electronics", "", null);
        product = new Product(id, "HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        createRequest = new CreateProductRequest("HUB USB Unitek 4x USB-A 3.1 Gen1", category.getId(), "4894160044402");
    }

    @Test
    void canGetAllProducts() {
        //when
        underTest.getAllProducts();
        //then
        verify(productRepository).findAll();
    }

    @Test
    void canGetProductById() {
        //given
        given(productRepository.findById(id)).willReturn(Optional.of(product));
        ProductDto expected = productMapper.map(product);

        //when
        ProductDto actual = underTest.getProductById(id);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void willThrowWhenGetProductByIdReturnsEmptyOptional() {
        //given
        given(productRepository.findById(id)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getProductById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product with id [%s] not found".formatted(id));
    }

    @Test
    void canCreateProduct() {
        //given
        given(categoryService.getCategoryEntityById(id)).willReturn(category);
        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        Long returnedId = underTest.createProduct(createRequest);

        //then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct.getId()).isNull();
        assertThat(capturedProduct.getTitle()).isEqualTo(createRequest.title());
        assertThat(capturedProduct.getCategory().getId()).isEqualTo(createRequest.categoryId());
        assertThat(capturedProduct.getEAN()).isEqualTo(createRequest.EAN());

        assertThat(returnedId).isEqualTo(product.getId());
    }

    @Test
    void canUpdateProduct() {
        //given
        UpdateProductRequest updateRequest = new UpdateProductRequest("HUB USB Unitek 4x USB-A 3.1 Gen2", category.getId(), "345667233445");
        given(categoryService.getCategoryEntityById(id)).willReturn(category);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        //when
        underTest.updateProduct(id, updateRequest);

        //then
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct.getTitle()).isEqualTo(updateRequest.title());
        assertThat(capturedProduct.getCategory().getId()).isEqualTo(updateRequest.categoryId());
        assertThat(capturedProduct.getEAN()).isEqualTo(updateRequest.EAN());
    }

    @Test
    void willThrowWhenTryingToUpdateProductNotFound() {
        //when
        //then
        assertThatThrownBy(() -> underTest.updateProduct(id, any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product with id [%s] not found".formatted(id));

        verify(productRepository, never()).save(any());
    }

    @Test
    void canDeleteProduct() {
        //when
        underTest.deleteProduct(id);

        //then
        verify(productRepository).deleteById(id);
    }

    @Test
    void canGetProductByEAN() {
        //given
        given(productRepository.findByEAN(product.getEAN())).willReturn(Optional.of(product));

        //when
        Product actual = underTest.getProductByEAN(product.getEAN());

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(product);
        verify(productRepository).findByEAN(anyString());
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void willThrowWhenGetProductByEANReturnsEmptyOptional() {
        //given
        given(productRepository.findByEAN(product.getEAN())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getProductByEAN(product.getEAN()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product with EAN [%s] not found".formatted(product.getEAN()));
    }
}