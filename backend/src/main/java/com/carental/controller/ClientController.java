package com.carental.controller;

import com.carental.model.Client;
import com.carental.repository.ClientRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Long id) {
        return clientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Client create(@Valid @RequestBody Client client) {
        return clientRepository.save(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @Valid @RequestBody Client client) {
        return clientRepository.findById(id)
                .map(existing -> {
                    existing.setName(client.getName());
                    existing.setCpf(client.getCpf());
                    existing.setRg(client.getRg());
                    existing.setAddress(client.getAddress());
                    existing.setOccupation(client.getOccupation());
                    existing.setEmployer(client.getEmployer());
                    existing.setIncome(client.getIncome());
                    return ResponseEntity.ok(clientRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!clientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
