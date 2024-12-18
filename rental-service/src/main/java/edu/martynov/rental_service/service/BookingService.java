package edu.martynov.rental_service.service;

import edu.martynov.rental_service.dao.ApartmentRepository;
import edu.martynov.rental_service.dao.BookingRepository;
import edu.martynov.rental_service.model.dto.BookingDto;
import edu.martynov.rental_service.model.dto.BookingRequestDto;
import edu.martynov.rental_service.model.entity.Apartment;
import edu.martynov.rental_service.model.entity.Booking;
import edu.martynov.rental_service.model.entity.User;
import edu.martynov.rental_service.model.enumerator.BookingStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ApartmentRepository apartmentRepository;

    @Transactional
    public BookingDto createBooking(User user, BookingRequestDto bookingRequestDto) {
        Apartment apartment = apartmentRepository.findById(bookingRequestDto.getApartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));

        if (!isApartmentAvailable(apartment.getId(), bookingRequestDto.getStartDate(), bookingRequestDto.getEndDate())) {
            throw new IllegalStateException("Apartment is not available for the selected dates");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setApartment(apartment);
        booking.setStartDate(bookingRequestDto.getStartDate());
        booking.setEndDate(bookingRequestDto.getEndDate());
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingDto.fromEntity(savedBooking);
    }

    public boolean isApartmentAvailable(Long apartmentId, LocalDate startDate, LocalDate endDate) {
        return !bookingRepository.existsBookingForDateRange(apartmentId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByUser(User user) {
        List<Booking> bookings = bookingRepository.findByUser(user);
        return bookings.stream()
                .map(BookingDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByOwner(User user) {
        List<Booking> bookings = bookingRepository.findByOwner(user);
        return bookings.stream()
                .map(BookingDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingDto updateBookingStatus(User owner, Long bookingId, BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));

        if (!booking.getApartment().getOwner().getId().equals(owner.getId())) {
            throw new IllegalArgumentException("Owner can't change booking for other apartment!");
        }

        booking.setStatus(status);

        return BookingDto.fromEntity(booking);
    }

    @Transactional
    public void deleteBooking(User user, Long bookingId) {
        Booking booking = bookingRepository.findByUserAndId(user, bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found!"));

        bookingRepository.delete(booking);
    }
}