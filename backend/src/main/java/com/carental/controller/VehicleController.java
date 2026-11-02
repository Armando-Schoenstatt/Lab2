package com.carental.controller;

import com.carental.model.Vehicle;
import com.carental.repository.VehicleRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Vehicle create(@Valid @RequestBody Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle) {
        return vehicleRepository.findById(id)
                .map(existing -> {
                    existing.setRegistration(vehicle.getRegistration());
                    existing.setManufactureYear(vehicle.getManufactureYear());
                    existing.setBrand(vehicle.getBrand());
                    existing.setModel(vehicle.getModel());
                    existing.setPlate(vehicle.getPlate());
                    existing.setOwnerType(vehicle.getOwnerType());
                    return ResponseEntity.ok(vehicleRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!vehicleRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
