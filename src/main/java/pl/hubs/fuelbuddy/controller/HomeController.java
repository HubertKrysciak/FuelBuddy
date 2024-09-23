package pl.hubs.fuelbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubs.fuelbuddy.entity.FuelEntry;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.entity.Vehicle;
import pl.hubs.fuelbuddy.service.FuelEntryService;
import pl.hubs.fuelbuddy.service.UserService;
import pl.hubs.fuelbuddy.service.VehicleService;

import java.util.List;

@Controller
public class HomeController {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final FuelEntryService fuelEntryService;

    @Autowired
    public HomeController(UserService userService, VehicleService vehicleService, FuelEntryService fuelEntryService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.fuelEntryService = fuelEntryService;
    }

    @GetMapping("/")
    public String showHomePage(Model model,
                               @RequestParam(required = false) Long selectedVehicleId,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) Boolean success,
                               @RequestParam(required = false) String error) {

        // Pobierz aktualnie zalogowanego użytkownika
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika"));

        // Pobranie pojazdów użytkownika
        List<Vehicle> vehicles = vehicleService.getVehiclesByUser(user);

        Vehicle selectedVehicle = null;

        if (selectedVehicleId != null) {
            selectedVehicle = vehicleService.getVehicleById(selectedVehicleId)
                    .orElse(null);
        } else if (!vehicles.isEmpty()) {
            selectedVehicle = vehicles.get(0); // Domyślnie pierwszy pojazd
        }

        Page<FuelEntry> fuelEntriesPage = Page.empty();
        double averageConsumption = 0.0;
        double totalFuelCost = 0.0;
        if (selectedVehicle != null) {
            Pageable pageable = PageRequest.of(page, 1, Sort.by("date").descending());
            fuelEntriesPage = fuelEntryService.getFuelEntriesByVehicle(selectedVehicle, pageable);

            // Obliczanie statystyk
            averageConsumption = fuelEntryService.calculateAverageConsumption(selectedVehicle);
            totalFuelCost = fuelEntryService.calculateTotalFuelCost(selectedVehicle);
        }

        // Inicjalizacja nowego fuelEntry
        FuelEntry fuelEntry = new FuelEntry();
        if (selectedVehicle != null) {
            fuelEntry.setVehicle(selectedVehicle);
        }
        model.addAttribute("fuelEntry", fuelEntry);

        // Inicjalizacja pustego pojazdu dla formularza dodawania pojazdu
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);
        model.addAttribute("vehicle", vehicle);

        model.addAttribute("averageConsumption", averageConsumption);
        model.addAttribute("totalFuelCost", totalFuelCost);
        model.addAttribute("selectedVehicle", selectedVehicle);
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("fuelEntriesPage", fuelEntriesPage);
        model.addAttribute("success", success);
        model.addAttribute("error", error);

        return "home"; // Nazwa widoku strony głównej
    }
}
