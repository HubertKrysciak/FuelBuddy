package pl.hubs.fuelbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.Vehicle;
import pl.hubs.fuelbuddy.service.FuelEntryService;
import pl.hubs.fuelbuddy.service.VehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fuelentries")
public class FuelEntryController {
    private final FuelEntryService fuelEntryService;
    private final VehicleService vehicleService;

    @Autowired
    public FuelEntryController(FuelEntryService fuelEntryService, VehicleService vehicleService) {
        this.fuelEntryService = fuelEntryService;
        this.vehicleService = vehicleService;
    }

    // Wyświetlanie listy wpisów dla pojazdu
    @GetMapping("/vehicle/{vehicleId}")
    public String listFuelEntries(@PathVariable Long vehicleId, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu o ID: " + vehicleId));
        List<FuelEntry> entries = fuelEntryService.getFuelEntriesByVehicle(vehicle);
        model.addAttribute("fuelEntries", entries);
        model.addAttribute("vehicle", vehicle);
        return "fuel_entry_list"; // Nazwa widoku Thymeleaf
    }

    // Wyświetlanie formularza dodawania wpisu
    @GetMapping("/vehicle/{vehicleId}/add")
    public String showAddFuelEntryForm(@PathVariable Long vehicleId, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu o ID: " + vehicleId));
        FuelEntry fuelEntry = new FuelEntry();
        fuelEntry.setVehicle(vehicle);
        model.addAttribute("fuelEntry", fuelEntry);
        return "fuel_entry_form"; // Nazwa widoku Thymeleaf
    }


    // Obsługa formularza dodawania wpisu
    @PostMapping("/save")
    public String saveFuelEntry(@ModelAttribute("fuelEntry") FuelEntry fuelEntry, Model model) {
        try {
            fuelEntryService.saveFuelEntry(fuelEntry);
            return "redirect:/?selectedVehicleId=" + fuelEntry.getVehicle().getId() + "&page=0&success=true";
        } catch (Exception e) {
            return "redirect:/?selectedVehicleId=" + fuelEntry.getVehicle().getId() + "&page=0&error=" + e.getMessage();
        }
    }
    // Wyświetlanie formularza edycji wpisu

    @PostMapping("/edit")
    public String showEditFuelEntryForm(@ModelAttribute("fuelEntry") FuelEntry fuelEntry, Model model) {
        try {
            fuelEntryService.getFuelEntryById(fuelEntry.getId());
            fuelEntryService.saveFuelEntry(fuelEntry);
            return "redirect:/?success=true";
        } catch (Exception e){
            return "redirect:/?error=" + e.getMessage();
        }
    }
    // Usuwanie wpisu

    @GetMapping("/delete/{id}")
    public String deleteFuelEntry(@PathVariable Long id) {
        FuelEntry fuelEntry = fuelEntryService.getFuelEntryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono wpisu o ID: " + id));
        Long vehicleId = fuelEntry.getVehicle().getId();
        fuelEntryService.deleteFuelEntry(id);
        return "redirect:/fuelentries/vehicle/" + vehicleId;
    }

    @GetMapping("/vehicle/{vehicleId}/statistics")
    public String showStatistics(@PathVariable Long vehicleId, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu o ID: " + vehicleId));

        List<FuelEntry> entries = fuelEntryService.getFuelEntriesByVehicle(vehicle);

        // Przygotowanie danych do wykresu
        List<String> labels = entries.stream()
                .map(entry -> entry.getDate().toString())
                .collect(Collectors.toList());

        List<Double> data = entries.stream()
                .map(entry -> entry.getFuelVolume())
                .collect(Collectors.toList());

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        // Dodanie danych analitycznych
        double averageConsumption = fuelEntryService.calculateAverageConsumption(vehicle);
        model.addAttribute("averageConsumption", averageConsumption);

        return "statistics"; // Nazwa widoku
    }

    @GetMapping("/vehicle/{vehicleId}/consumption")
    public String showConsumptionChart(@PathVariable Long vehicleId, Model model) {
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu o ID: " + vehicleId));

        List<FuelEntry> entries = fuelEntryService.getFuelEntriesByVehicle(vehicle);

        // Obliczenie zużycia paliwa między kolejnymi tankowaniami
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();

        for (int i = 1; i < entries.size(); i++) {
            FuelEntry previous = entries.get(i - 1);
            FuelEntry current = entries.get(i);

            int distance = current.getMileage() - previous.getMileage();
            if (distance > 0) {
                double consumption = (current.getFuelVolume() / distance) * 100;
                labels.add(current.getDate().toString());
                data.add(consumption);
            }
        }

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);

        return "consumption_chart";
    }
    // Dodanie nowego tankowania

}
