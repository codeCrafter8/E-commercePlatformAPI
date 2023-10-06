package com.pricecomparison.repository;

import com.pricecomparison.model.Category;
import com.pricecomparison.model.Offer;
import com.pricecomparison.model.Product;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class OfferRepositoryTest {
    @Autowired
    private OfferRepository underTest;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findBySourceAndSourceOfferIdNotIn() {
        //given
        Category category = new Category("Electronics", null);
        categoryRepository.save(category);

        Product product = new Product("HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        productRepository.save(product);

        String oddSourceOfferId = "1122344";
        List<Offer> offers = List.of(
             new Offer(
                     1L,
                     "Ebay",
                     "324567",
                     16.99F,
                     product
             ),
             new Offer(
                    2L,
                    "Ebay",
                    "345563",
                    18.99F,
                    product
            ),
            new Offer(
                    3L,
                    "Amazon",
                    oddSourceOfferId,
                    25.99F,
                    product
            ),
             new Offer(
                    4L,
                    "Ebay",
                    oddSourceOfferId,
                    20.99F,
                    product
            )
        );
        underTest.saveAll(offers);

        String source = "Ebay";
        List<String> sourceOfferIds = new ArrayList<>();
        sourceOfferIds.add("324567");
        sourceOfferIds.add("345563");

        //when
        List<Offer> actual = underTest.findBySourceAndSourceOfferIdNotIn(source, sourceOfferIds);

        //then
        assertThat(actual).satisfiesExactly(
                item1 -> AssertionsForClassTypes.assertThat(item1.getSourceOfferId()).isEqualTo(oddSourceOfferId)
        );
    }
}