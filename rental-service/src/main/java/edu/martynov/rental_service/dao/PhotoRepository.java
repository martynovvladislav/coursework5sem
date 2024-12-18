package edu.martynov.rental_service.dao;

import edu.martynov.rental_service.model.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
