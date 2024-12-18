package edu.martynov.rental_service.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequestDto {
    private Long apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
