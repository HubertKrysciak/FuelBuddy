package pl.hubs.fuelbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;

public interface FuelEntryRepository extends JpaRepository<FuelEntry, Long> {
    List<FuelEntry> findByVehicle(Vehicle vehicle);
}
