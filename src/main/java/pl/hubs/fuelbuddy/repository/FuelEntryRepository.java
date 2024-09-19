package pl.hubs.fuelbuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;

public interface FuelEntryRepository extends JpaRepository<FuelEntry, Long> {
    List<FuelEntry> findByVehicle(Vehicle vehicle);
    Page<FuelEntry> findByVehicle(Vehicle vehicle, Pageable pageable);
    List<FuelEntry> findByVehicleOrderByDateAsc(Vehicle vehicle);
    List<FuelEntry> findByVehicleOrderByDateDesc(Vehicle vehicle);
}
