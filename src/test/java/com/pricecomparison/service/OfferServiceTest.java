package com.pricecomparison.service;

import com.pricecomparison.mapper.OfferMapper;
import com.pricecomparison.model.Category;
import com.pricecomparison.model.Offer;
import com.pricecomparison.model.Product;
import com.pricecomparison.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private ProductService productService;
    private final OfferMapper offerMapper = new OfferMapper();
    private OfferService underTest;
    private Offer offer;
    private Product product;

    @BeforeEach
    void setUp() {
        underTest = new OfferService(offerRepository, productService, offerMapper);
        Long id = 1L;
        Category category = new Category(id, "Electronics", "", null);
        product = new Product(id, "HUB USB Unitek 4x USB-A 3.1 Gen1", category, "4894160044402");
        offer = new Offer(id, "Ebay", "43788", 15.99F, product);
    }

    @Test
    void canGetAllOffersByProductId() {
        //given
        Long productId = 1L;

        //when
        underTest.getAllOffersByProductId(productId);

        //then
        verify(offerRepository).findAllByProductId(productId);
    }

    @Test
    void canCreateOffer() {
        //given
        given(offerRepository.findBySourceAndSourceOfferId(anyString(), anyString())).willReturn(Optional.empty());
        given(productService.getProductByEAN(anyString())).willReturn(product);

        String source = "Ebay";
        String sourceOfferId = "45634";
        Float price = 10.99F;
        String EAN = product.getEAN();

        //when
        underTest.createOrUpdateOffer(source, sourceOfferId, price, EAN);

        //then
        ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerArgumentCaptor.capture());
        Offer actual = offerArgumentCaptor.getValue();

        assertThat(actual.getSource()).isEqualTo(source);
        assertThat(actual.getSourceOfferId()).isEqualTo(sourceOfferId);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getProduct().getEAN()).isEqualTo(EAN);
    }

    @Test
    void canUpdateOffer() {
        //given
        given(offerRepository.findBySourceAndSourceOfferId(anyString(), anyString())).willReturn(Optional.of(offer));

        String source = offer.getSource();
        String sourceOfferId = offer.getSourceOfferId();
        Float price = 100.99F;
        String EAN = product.getEAN();

        //when
        underTest.createOrUpdateOffer(source, sourceOfferId, price, EAN);

        //then
        ArgumentCaptor<Offer> offerArgumentCaptor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(offerArgumentCaptor.capture());
        Offer actual = offerArgumentCaptor.getValue();

        assertThat(actual.getSource()).isEqualTo(source);
        assertThat(actual.getSourceOfferId()).isEqualTo(sourceOfferId);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getProduct().getEAN()).isEqualTo(EAN);
    }
}