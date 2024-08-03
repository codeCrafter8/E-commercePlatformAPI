package com.pricecomparison.dto;

import java.util.Date;

public record ProductDto(
        Long id,
        String title,
        Long categoryId,
        String EAN,
        String createdBy,
        Date createdDate,
        String lastModifiedBy,
        Date lastModifiedDate
) {}
