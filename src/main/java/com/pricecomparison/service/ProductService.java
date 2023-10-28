package com.pricecomparison.service;

import com.pricecomparison.dto.ProductDto;
import com.pricecomparison.exception.ResourceNotFoundException;
import com.pricecomparison.mapper.ProductMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import com.pricecomparison.payload.request.create.CreateProductRequest;
import com.pricecomparison.payload.request.update.UpdateProductRequest;
import com.pricecomparison.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::map)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );
        return productMapper.map(product);
    }

    public Long createProduct(CreateProductRequest createRequest) {
        //TODO: ean already taken?

        Category category = categoryService.getCategoryEntityById(createRequest.categoryId());

        Product product = productMapper.map(createRequest, category);
        product = productRepository.save(product);

        return product.getId();
    }

    public void updateProduct(Long productId, UpdateProductRequest updateRequest) {
        // TODO: for JPA use .getReferenceById(customerId) as it does does not bring object into memory and instead a reference
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId))
                );

        Category category = categoryService.getCategoryEntityById(updateRequest.categoryId());

        product.setTitle(updateRequest.title());
        product.setCategory(category);
        product.setEAN(updateRequest.EAN());

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    public Product getProductByEAN(String EAN) {
        return productRepository.findByEAN(EAN)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with EAN [%s] not found".formatted(EAN))
                );
    }

    //TODO: is it good
    public List<Product> getAllProductsEntities() {
        return productRepository.findAll();
    }
}
