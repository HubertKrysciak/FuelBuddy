package pl.hubs.fuelbuddy.service;

import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    Vehicle saveVehicle(Vehicle vehicle);
    Optional<Vehicle> getVehicleById(Long id);
    List<Vehicle> getVehiclesByUser(User user);
    void deleteVehicle(Long id);
}
