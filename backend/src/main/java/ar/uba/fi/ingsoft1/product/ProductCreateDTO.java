package ar.uba.fi.ingsoft1.product;

import lombok.NonNull;

import java.util.Set;

public record ProductCreateDTO(
        String productName,
        String photo,
        Integer stock,
        Set<Long> attributeIds,
        Long alternativeId
) {}
