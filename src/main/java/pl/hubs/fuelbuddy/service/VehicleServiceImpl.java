package pl.hubs.fuelbuddy.service;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.entity.Vehicle;
import pl.hubs.fuelbuddy.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        // Walidacja danych pojazdu
        validateVehicle(vehicle);

        // Sprawdzenie unikalności numeru rejestracyjnego dla użytkownika
        if (vehicle.getRegistrationNumber() != null) {
            Optional<Vehicle> existingVehicle = vehicleRepository.findByUserAndRegistrationNumber(
                    vehicle.getUser(), vehicle.getRegistrationNumber());
            if (existingVehicle.isPresent() && !existingVehicle.get().getId().equals(vehicle.getId())) {
                throw new IllegalArgumentException("Pojazd o tym numerze rejestracyjnym już istnieje.");
            }
        }

        // Zapis pojazdu w bazie danych
        return vehicleRepository.save(vehicle);
    }

    private void validateVehicle(Vehicle vehicle) {
        if (!StringUtils.hasText(vehicle.getMake())) {
            throw new IllegalArgumentException("Marka pojazdu jest wymagana.");
        }
        if (!StringUtils.hasText(vehicle.getModel())) {
            throw new IllegalArgumentException("Model pojazdu jest wymagany.");
        }
        if (vehicle.getYear() != null && (vehicle.getYear() < 1886 || vehicle.getYear() > LocalDate.now().getYear() + 1)) {
            throw new IllegalArgumentException("Nieprawidłowy rok produkcji.");
        }
    }
    @Override
    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public List<Vehicle> getVehiclesByUser(User user) {
        return vehicleRepository.findByUser(user);
    }

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Pojazd o podanym ID nie istnieje.");
        }
        vehicleRepository.deleteById(id);
    }
}
