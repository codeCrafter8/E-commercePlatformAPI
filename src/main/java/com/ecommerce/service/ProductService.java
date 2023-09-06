package com.ecommerce.service;

import com.ecommerce.enumeration.ProductState;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.AppUserMapper;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.model.AppUser;
import com.ecommerce.model.Product;
import com.ecommerce.payload.request.create.CreateProductRequest;
import com.ecommerce.payload.request.update.UpdateProductRequest;
import com.ecommerce.repository.AppUserRepository;
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
    public List<Product> getAllProducts() {
        //List<Product> products = productRepository.findAll();
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Product with id [%s] not found".formatted(productId))
                );
    }

    public Long createProduct(CreateProductRequest createRequest) {
        AppUser user = appUserRepository.findByUsername(createRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with username [%s] not found".formatted(createRequest.username()))
                );

        Product product = ProductMapper.map(createRequest, user);
        product = productRepository.save(product);

        return product.getId();
    }

    public void updateProduct(Long productId, UpdateProductRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );
        product.setTitle(updateRequest.title());
        product.setPrice(updateRequest.price());
        product.setDescription(updateRequest.description());
        product.setCategory(updateRequest.category());
        product.setState(ProductState.valueOf(updateRequest.state()));

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
