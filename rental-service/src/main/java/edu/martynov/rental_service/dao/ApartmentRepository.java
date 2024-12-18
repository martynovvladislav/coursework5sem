package edu.martynov.rental_service.dao;

import edu.martynov.rental_service.model.entity.Apartment;
import edu.martynov.rental_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByIsPublishedIsTrue();

    List<Apartment> findByOwner(User owner);

    Optional<Apartment> findByIdAndOwner(Long id, User owner);
}
