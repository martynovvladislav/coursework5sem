package edu.martynov.rental_service.dao;

import edu.martynov.rental_service.model.entity.Apartment;
import edu.martynov.rental_service.model.entity.Booking;
import edu.martynov.rental_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.apartment = :apartment AND " +
            "((b.startDate <= :end AND b.endDate >= :start)) AND b.status = 'CONFIRMED'")
    List<Booking> findAllByApartmentAndDatesBetweenAndBookingStatusConfirmed(@Param("apartment") Apartment apartment,
                                                    @Param("start") LocalDate start,
                                                    @Param("end") LocalDate end);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.apartment.id = :apartmentId " +
            "AND ((b.startDate BETWEEN :startDate AND :endDate) " +
            "OR (b.endDate BETWEEN :startDate AND :endDate) " +
            "OR (b.startDate <= :startDate AND b.endDate >= :endDate)) AND b.status = 'CONFIRMED'")
    boolean existsBookingForDateRange(@Param("apartmentId") Long apartmentId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    List<Booking> findByUser(User user);

    Optional<Booking> findByUserAndId(User user, Long Id);

    @Query("SELECT b FROM Booking b WHERE b.apartment.owner = :owner")
    List<Booking> findByOwner(@Param("owner") User owner);
}
