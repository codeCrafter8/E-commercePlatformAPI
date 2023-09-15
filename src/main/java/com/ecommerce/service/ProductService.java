package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.enumeration.ProductState;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.AppUserMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.request.create.CreateProductRequest;
import com.ecommerce.payload.request.update.UpdateProductRequest;
import com.ecommerce.repository.AppUserRepository;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductMapper::map)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );
        return ProductMapper.map(product);
    }

    public Long createProduct(CreateProductRequest createRequest) {
        /*AppUser user = appUserRepository.findById(createRequest.appUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id [%s] not found".formatted(createRequest.appUserId()))
                );*/

        Category category = categoryRepository.findById(createRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(createRequest.categoryId()))
                );

        Product product = ProductMapper.map(createRequest, category);
        product = productRepository.save(product);

        return product.getId();
    }

    public void updateProduct(Long productId, UpdateProductRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );

        Category category = categoryRepository.findById(updateRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id [%s] not found".formatted(updateRequest.categoryId()))
                );

        product.setTitle(updateRequest.title());
        product.setCategory(category);

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
