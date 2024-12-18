package edu.martynov.rental_service.model.dto;

import edu.martynov.rental_service.model.entity.Apartment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentDto {
    private Long id;
    private String title;
    private String address;
    private String description;
    private BigDecimal pricePerMonth;
    private boolean isPublished;
    private String photoUrl;

    public static ApartmentDto fromEntity(Apartment apartment) {
        return new ApartmentDto(
                apartment.getId(),
                apartment.getTitle(),
                apartment.getAddress(),
                apartment.getDescription(),
                apartment.getPricePerMonth(),
                apartment.isPublished(),
                apartment.getPhoto().getUrl()
        );
    }
}

