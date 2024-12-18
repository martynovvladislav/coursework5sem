package edu.martynov.rental_service.controller;

import edu.martynov.rental_service.model.dto.BookingDto;
import edu.martynov.rental_service.model.dto.BookingRequestDto;
import edu.martynov.rental_service.model.entity.User;
import edu.martynov.rental_service.model.enumerator.BookingStatus;
import edu.martynov.rental_service.service.BookingService;
import edu.martynov.rental_service.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_COMMON_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookingDto> createBooking(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody BookingRequestDto bookingRequestDto) {
        User user = userService.findByUsername(userDetails.getUsername());
        BookingDto bookingDto = bookingService.createBooking(user, bookingRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_COMMON_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDto>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<BookingDto> bookings = bookingService.getBookingsByUser(user);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/requests")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDto>> getApartmentBookingRequests(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<BookingDto> bookings = bookingService.getBookingsByOwner(user);
        return ResponseEntity.ok(bookings);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_OWNER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookingDto> updateBookingStatus(@PathVariable Long id,
                                                          @RequestParam BookingStatus status,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        User owner = userService.findByUsername(userDetails.getUsername());
        BookingDto bookingDto = bookingService.updateBookingStatus(owner, id, status);
        return ResponseEntity.ok(bookingDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_COMMON_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMyBooking(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());

        bookingService.deleteBooking(user, id);

        return ResponseEntity.noContent().build();
    }
}
