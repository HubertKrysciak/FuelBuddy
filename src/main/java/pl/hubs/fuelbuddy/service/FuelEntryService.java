package pl.hubs.fuelbuddy.service;

import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;

import java.util.List;
import java.util.Optional;

public interface FuelEntryService {
    FuelEntry saveFuelEntry(FuelEntry fuelEntry);
    Optional<FuelEntry> getFuelEntryById(Long id);
    List<FuelEntry> getFuelEntriesByVehicle(Vehicle vehicle);
    void deleteFuelEntry(Long id);
}
