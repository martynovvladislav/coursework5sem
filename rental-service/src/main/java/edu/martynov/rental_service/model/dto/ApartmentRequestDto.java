package edu.martynov.rental_service.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApartmentRequestDto {

    private String title;
    private String address;
    private String description;
    private BigDecimal pricePerMonth;
    private String photoUrl;

}
