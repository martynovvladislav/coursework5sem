package edu.martynov.rental_service.model.dto;

import edu.martynov.rental_service.model.entity.Booking;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {

    private Long id;
    private Long apartmentId;
    private String apartmentTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String username;

    public static BookingDto fromEntity(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setApartmentId(booking.getApartment().getId());
        dto.setApartmentTitle(booking.getApartment().getTitle());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setStatus(booking.getStatus().name());
        dto.setUsername(booking.getUser().getUsername());
        return dto;
    }
}

