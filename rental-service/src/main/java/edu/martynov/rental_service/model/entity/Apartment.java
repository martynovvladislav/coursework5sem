package edu.martynov.rental_service.model.entity;

import edu.martynov.rental_service.model.dto.ApartmentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal pricePerMonth;

    @Column(nullable = false)
    private boolean isPublished;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "apartment")
    private Photo photo;

    public void update(ApartmentRequestDto dto) {
        setAddress(dto.getAddress());
        setDescription(dto.getDescription());
        setTitle(dto.getTitle());
        setPricePerMonth(dto.getPricePerMonth());
        photo.setUrl(dto.getPhotoUrl());
    }
}
