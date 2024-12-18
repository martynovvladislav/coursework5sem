package edu.martynov.rental_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @OneToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    public static Photo create(Apartment apartment) {
        Photo photo = new Photo(null, null, apartment);
        apartment.setPhoto(photo);
        return photo;
    }
}

