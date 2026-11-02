package com.carental.controller;

import com.carental.model.Client;
import com.carental.model.RentalRequest;
import com.carental.model.Vehicle;
import com.carental.repository.ClientRepository;
import com.carental.repository.RentalRequestRepository;
import com.carental.repository.VehicleRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rental-requests")
public class RentalRequestController {

    private final RentalRequestRepository rentalRequestRepository;
    private final ClientRepository clientRepository;
    private final VehicleRepository vehicleRepository;

    public RentalRequestController(RentalRequestRepository rentalRequestRepository,
                                  ClientRepository clientRepository,
                                  VehicleRepository vehicleRepository) {
        this.rentalRequestRepository = rentalRequestRepository;
        this.clientRepository = clientRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    public List<RentalRequest> getAll() {
        return rentalRequestRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<RentalRequest> create(@Valid @RequestBody RentalRequest request) {
        Client client = clientRepository.findById(request.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicle().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        request.setClient(client);
        request.setVehicle(vehicle);
        return ResponseEntity.ok(rentalRequestRepository.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalRequest> update(@PathVariable Long id, @Valid @RequestBody RentalRequest request) {
        return rentalRequestRepository.findById(id)
                .map(existing -> {
                    existing.setRentalDate(request.getRentalDate());
                    existing.setReturnDate(request.getReturnDate());
                    existing.setStatus(request.getStatus());
                    existing.setCreditContractNumber(request.getCreditContractNumber());
                    return ResponseEntity.ok(rentalRequestRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!rentalRequestRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rentalRequestRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
