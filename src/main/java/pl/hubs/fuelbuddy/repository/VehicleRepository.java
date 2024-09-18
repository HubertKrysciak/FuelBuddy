package pl.hubs.fuelbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUser(User user);
}