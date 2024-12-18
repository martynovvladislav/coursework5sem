package edu.martynov.rental_service.service;

import edu.martynov.rental_service.dao.ApartmentRepository;
import edu.martynov.rental_service.dao.BookingRepository;
import edu.martynov.rental_service.model.dto.ApartmentDto;
import edu.martynov.rental_service.model.dto.ApartmentRequestDto;
import edu.martynov.rental_service.model.entity.Apartment;
import edu.martynov.rental_service.model.entity.Booking;
import edu.martynov.rental_service.model.entity.Photo;
import edu.martynov.rental_service.model.entity.User;
import edu.martynov.rental_service.model.enumerator.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public List<ApartmentDto> getPublishedApartments() {
        return apartmentRepository.findByIsPublishedIsTrue()
                .stream()
                .map(ApartmentDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApartmentDto> getAllApartments() {
        return apartmentRepository.findAll()
                .stream()
                .map(ApartmentDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ApartmentDto getApartmentById(Long id) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));
        return ApartmentDto.fromEntity(apartment);
    }

    @Transactional(readOnly = true)
    public List<LocalDate> getApartmentAvailability(Long apartmentId, LocalDate start, LocalDate end) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));

        List<Booking> bookings = bookingRepository.findAllByApartmentAndDatesBetweenAndBookingStatusConfirmed(apartment, start, end);
        Set<LocalDate> bookedDates = bookings.stream()
                .flatMap(booking -> booking.getStartDate().datesUntil(booking.getEndDate().plusDays(1)))
                .collect(Collectors.toSet());

        return start.datesUntil(end.plusDays(1))
                .filter(date -> !bookedDates.contains(date))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ApartmentDto> getApartmentsByOwner(User owner) {
        List<Apartment> apartments = apartmentRepository.findByOwner(owner);
        return apartments.stream()
                .map(ApartmentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ApartmentDto updateApartment(User user, Long id, ApartmentRequestDto apartmentRequestDto) {
        Apartment apartment = apartmentRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));

        apartment.update(apartmentRequestDto);

        return ApartmentDto.fromEntity(apartment);
    }

    @Transactional
    public ApartmentDto createApartment(User user, ApartmentRequestDto apartmentRequestDto) {
        Apartment apartment = new Apartment();
        apartment.setOwner(user);
        apartment.setPublished(false);

        Photo photo = Photo.create(apartment);

        apartment.update(apartmentRequestDto);

        apartmentRepository.save(apartment);

        return ApartmentDto.fromEntity(apartment);
    }

    @Transactional
    public void deleteApartment(User user, Long id) {
        Apartment apartment = apartmentRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));

        apartmentRepository.delete(apartment);
    }

    @Transactional
    public ApartmentDto updatePublishStatus(User user, Long id, boolean isPublished) {
        Apartment apartment;
        if (user.getRole().equals(Role.ADMIN)) {
            apartment = apartmentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));
        } else {
            apartment = apartmentRepository.findByIdAndOwner(id, user)
                    .orElseThrow(() -> new EntityNotFoundException("Apartment not found"));
        }


        apartment.setPublished(isPublished);

        return ApartmentDto.fromEntity(apartment);
    }
}

