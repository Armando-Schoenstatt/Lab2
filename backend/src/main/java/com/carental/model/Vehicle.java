package com.carental.model;

import com.carental.domain.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String registration;

    @Column(name = "manufacture_year", nullable = false)
    private Integer manufactureYear;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType ownerType;
}
