package edu.martynov.rental_service.controller;

import edu.martynov.rental_service.model.dto.ApartmentDto;
import edu.martynov.rental_service.model.dto.ApartmentRequestDto;
import edu.martynov.rental_service.model.entity.Apartment;
import edu.martynov.rental_service.model.entity.User;
import edu.martynov.rental_service.model.enumerator.Role;
import edu.martynov.rental_service.service.ApartmentService;
import edu.martynov.rental_service.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ApartmentDto>> getPublishedApartments() {
        List<ApartmentDto> apartments = apartmentService.getPublishedApartments();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ApartmentDto>> getAllApartments() {
        List<ApartmentDto> apartments = apartmentService.getAllApartments();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDto> getApartmentById(@PathVariable Long id) {
        ApartmentDto apartment = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(apartment);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<List<LocalDate>> getApartmentAvailability(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_day,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_day
    ) {
        List<LocalDate> availableDates = apartmentService.getApartmentAvailability(id, start_day, end_day);
        return ResponseEntity.ok(availableDates);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ApartmentDto>> getMyApartments(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<ApartmentDto> apartments = apartmentService.getApartmentsByOwner(user);
        return ResponseEntity.ok(apartments);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApartmentDto> updateApartment(@PathVariable Long id,
                                                        @RequestBody ApartmentRequestDto apartmentRequestDto,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        ApartmentDto apartmentDto = apartmentService.updateApartment(user, id, apartmentRequestDto);
        return ResponseEntity.ok(apartmentDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        apartmentService.deleteApartment(user, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApartmentDto> createApartment(
            @RequestBody ApartmentRequestDto apartmentRequestDto, @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        ApartmentDto apartmentDto = apartmentService.createApartment(user, apartmentRequestDto);

        return ResponseEntity.ok(apartmentDto);
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApartmentDto> updatePublishStatus(
            @RequestParam boolean isPublished, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        ApartmentDto apartmentDto = apartmentService.updatePublishStatus(user, id, isPublished);

        return ResponseEntity.ok(apartmentDto);
    }
}

