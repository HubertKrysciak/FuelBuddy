package pl.hubs.fuelbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.entity.Vehicle;
import pl.hubs.fuelbuddy.service.UserService;
import pl.hubs.fuelbuddy.service.VehicleService;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final UserService userService;

    @Autowired
    public VehicleController(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    // Wyświetlanie listy pojazdów użytkownika
    @GetMapping("/user/{userId}")
    public String listVehicles(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o ID: " + userId));
        model.addAttribute("vehicles", vehicleService.getVehiclesByUser(user));
        model.addAttribute("user", user);
        return "vehicle_list"; // Nazwa widoku Thymeleaf
    }

    // Wyświetlanie formularza dodawania pojazdu
    @GetMapping("/user/{userId}/add")
    public String showAddVehicleForm(@PathVariable Long userId, Model model) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o ID: " + userId));
        Vehicle vehicle = new Vehicle();
        vehicle.setUser(user);
        model.addAttribute("vehicle", vehicle);
        return "vehicle_form"; // Nazwa widoku Thymeleaf
    }

    // Obsługa dodawania pojazdu
    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute("vehicle") Vehicle vehicle, Model model) {
        try {
            vehicle.toString();
            vehicleService.saveVehicle(vehicle);
            return "redirect:/?selectedVehicleId=" + "&success=true";
        } catch (Exception e) {
            return "redirect:/?error=" + e.getMessage();
        }
    }

    // Opcjonalnie: Metoda do edycji pojazdu
    @PostMapping("/edit/{id}")
    public String editVehicle(@PathVariable Long id, @ModelAttribute("vehicle") Vehicle vehicle, Model model) {
        try {
            vehicle.setId(id);
            vehicleService.saveVehicle(vehicle);
            return "redirect:/?selectedVehicleId=" + vehicle.getId() + "&success=true";
        } catch (Exception e) {
            return "redirect:/?error=" + e.getMessage();
        }
    }

    // Usuwanie pojazdu
    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pojazdu o ID: " + id));
        Long userId = vehicle.getUser().getId();
        vehicleService.deleteVehicle(id);
        return "redirect:/vehicles/user/" + userId;
    }
}
