package pl.hubs.fuelbuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;
import pl.hubs.fuelbuddy.repository.FuelEntryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FuelEntryServiceImpl implements FuelEntryService {
    private final FuelEntryRepository fuelEntryRepository;


    @Autowired
    public FuelEntryServiceImpl(FuelEntryRepository fuelEntryRepository) {
        this.fuelEntryRepository = fuelEntryRepository;
    }

    @Override
    public FuelEntry saveFuelEntry(FuelEntry fuelEntry) {
        // Walidacja danych wpisu
        validateFuelEntry(fuelEntry);

        // Pobranie ostatniego wpisu dla pojazdu
        List<FuelEntry> entries = fuelEntryRepository.findByVehicleOrderByDateDesc(fuelEntry.getVehicle());
        if (!entries.isEmpty()) {
            FuelEntry lastEntry = entries.get(0);
            // Sprawdzenie przebiegu
            if (fuelEntry.getMileage() <= lastEntry.getMileage()) {
                throw new IllegalArgumentException("Przebieg musi być większy niż poprzedni.");
            }
        }

        // Zapis wpisu w bazie danych
        return fuelEntryRepository.save(fuelEntry);
    }

    private void validateFuelEntry(FuelEntry fuelEntry) {
        if (fuelEntry.getDate() == null) {
            throw new IllegalArgumentException("Data tankowania jest wymagana.");
        }
        if (fuelEntry.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data tankowania nie może być w przyszłości.");
        }
        if (fuelEntry.getMileage() == null || fuelEntry.getMileage() <= 0) {
            throw new IllegalArgumentException("Przebieg musi być większy niż zero.");
        }
        if (fuelEntry.getFuelVolume() == null || fuelEntry.getFuelVolume() <= 0) {
            throw new IllegalArgumentException("Ilość paliwa musi być większa niż zero.");
        }
        if (fuelEntry.getPricePerUnit() == null || fuelEntry.getPricePerUnit() <= 0) {
            throw new IllegalArgumentException("Cena jednostkowa musi być większa niż zero.");
        }
    }

    @Override
    public Optional<FuelEntry> getFuelEntryById(Long id) {
        return fuelEntryRepository.findById(id);
    }

    @Override
    public List<FuelEntry> getFuelEntriesByVehicle(Vehicle vehicle) {
        return fuelEntryRepository.findByVehicle(vehicle);
    }

    @Override
    public Page<FuelEntry> getFuelEntriesByVehicle(Vehicle vehicle, Pageable pageable) {
        return fuelEntryRepository.findByVehicle(vehicle, pageable);
    }


    @Override
    public void deleteFuelEntry(Long id) {
        if (!fuelEntryRepository.existsById(id)) {
            throw new IllegalArgumentException("Wpis o podanym ID nie istnieje.");
        }
        fuelEntryRepository.deleteById(id);
    }

    // Obliczanie średniego zużycia paliwa na 100 km
    @Override
    public double calculateAverageConsumption(Vehicle vehicle) {
        List<FuelEntry> entries = fuelEntryRepository.findByVehicleOrderByDateAsc(vehicle);
        if (entries.size() < 2) {
            return 0.0;
        }

        double totalFuel = 0.0;
        int totalDistance = 0;

        for (int i = 1; i < entries.size(); i++) {
            FuelEntry previous = entries.get(i - 1);
            FuelEntry current = entries.get(i);

            int distance = current.getMileage() - previous.getMileage();
            if (distance > 0) {
                totalDistance += distance;
                totalFuel += current.getFuelVolume();
            }
        }

        if (totalDistance == 0) {
            return 0.0;
        }

        return (totalFuel / totalDistance) * 100;
    }

    // Obliczanie kosztu na kilometr
    public double calculateCostPerKilometer(Vehicle vehicle) {
        List<FuelEntry> entries = fuelEntryRepository.findByVehicleOrderByDateAsc(vehicle);
        if (entries.size() < 2) {
            return 0.0;
        }

        double totalCost = 0.0;
        int totalDistance = 0;

        for (int i = 1; i < entries.size(); i++) {
            FuelEntry previous = entries.get(i - 1);
            FuelEntry current = entries.get(i);

            int distance = current.getMileage() - previous.getMileage();
            if (distance > 0) {
                totalDistance += distance;
                double cost = current.getFuelVolume() * current.getPricePerUnit() - current.getDiscount();
                totalCost += cost;
            }
        }

        if (totalDistance == 0) {
            return 0.0;
        }

        return totalCost / totalDistance;
    }

    @Override
    public double calculateTotalFuelCost(Vehicle vehicle) {
        List<FuelEntry> entries = fuelEntryRepository.findByVehicle(vehicle);
        double totalCost = 0.0;

        for (FuelEntry entry : entries) {
            double cost = entry.getFuelVolume() * entry.getPricePerUnit() - (entry.getDiscount() != null ? entry.getDiscount() : 0.0);
            totalCost += cost;
        }

        return totalCost;
    }

    @Override
    public double calculateAverageFuelPrice(Vehicle vehicle) {
        List<FuelEntry> entries = fuelEntryRepository.findByVehicle(vehicle);
        if (entries.isEmpty()) {
            return 0.0;
        }

        double totalPrice = 0.0;
        int count = 0;

        for (FuelEntry entry : entries) {
            totalPrice += entry.getPricePerUnit();
            count++;
        }

        return totalPrice / count;
    }

}
