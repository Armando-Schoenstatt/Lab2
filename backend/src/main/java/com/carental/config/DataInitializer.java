package com.carental.config;

import com.carental.domain.OwnerType;
import com.carental.domain.Role;
import com.carental.model.Client;
import com.carental.model.RentalRequest;
import com.carental.model.User;
import com.carental.model.Vehicle;
import com.carental.repository.ClientRepository;
import com.carental.repository.RentalRequestRepository;
import com.carental.repository.UserRepository;
import com.carental.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Value("${app.bootstrap.admin-username:}")
    private String adminUsername;

    @Value("${app.bootstrap.admin-password:}")
    private String adminPassword;

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               ClientRepository clientRepository,
                               VehicleRepository vehicleRepository,
                               RentalRequestRepository rentalRequestRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminUsername.isBlank() != adminPassword.isBlank()) {
                throw new IllegalStateException(
                        "Set both BOOTSTRAP_ADMIN_USERNAME and BOOTSTRAP_ADMIN_PASSWORD, or leave both empty"
                );
            }

            if (!adminUsername.isBlank() && userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }

            if (clientRepository.count() == 0) {
                Client client = Client.builder()
                        .name("Ana Souza")
                        .cpf("111.222.333-44")
                        .rg("12.345.678")
                        .address("Rua das Flores, 123")
                        .occupation("Analista de Sistemas")
                        .employer("TechCorp")
                        .income(4200.0)
                        .build();
                clientRepository.save(client);

                Vehicle vehicle = Vehicle.builder()
                        .registration("ABC1234")
                        .manufactureYear(2024)
                        .brand("Fiat")
                        .model("Mobi")
                        .plate("XYZ-1234")
                        .ownerType(OwnerType.COMPANY)
                        .build();
                vehicleRepository.save(vehicle);

                RentalRequest request = RentalRequest.builder()
                        .client(client)
                        .vehicle(vehicle)
                        .rentalDate(LocalDate.now().plusDays(2))
                        .returnDate(LocalDate.now().plusDays(12))
                        .status("PENDENTE")
                        .creditContractNumber("CC-001")
                        .build();
                rentalRequestRepository.save(request);
            }
        };
    }
}
