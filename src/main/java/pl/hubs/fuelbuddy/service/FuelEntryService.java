package pl.hubs.fuelbuddy.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface FuelEntryService {
    FuelEntry saveFuelEntry(FuelEntry fuelEntry);
    Optional<FuelEntry> getFuelEntryById(Long id);
    List<FuelEntry> getFuelEntriesByVehicle(Vehicle vehicle);
    Page<FuelEntry> getFuelEntriesByVehicle(Vehicle vehicle, Pageable pageable);
    void deleteFuelEntry(Long id);
    double calculateAverageConsumption(Vehicle vehicle);
    double calculateTotalFuelCost(Vehicle vehicle);
    double calculateAverageFuelPrice(Vehicle vehicle);

}
