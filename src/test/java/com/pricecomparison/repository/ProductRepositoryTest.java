package com.pricecomparison.repository;

import com.pricecomparison.model.Category;
import com.pricecomparison.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository underTest;

    @Test
    void findAllEANs() {
        //given
        Category category = new Category("Electronics", null);
        Product product1 = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        Product product2 = new Product("JBL JBLT110BLK", category, "6925281918926");
        categoryRepository.save(category);
        underTest.save(product1);
        underTest.save(product2);

        //when
        List<String> eansList = underTest.findAllEANs();

        //then
        assertThat(eansList).satisfiesExactly(
                item1 -> assertThat(item1).contains("4894160044402"),
                item2 -> assertThat(item2).contains("6925281918926")
        );
    }
}