package pl.hubs.fuelbuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //Wyświetlanie formularza rejestracji
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "register"; //widok register
    }

    // Obsługa formularza rejestracji
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/users/register?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    // Wyświetlanie profilu użytkownika
    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o ID: " + id));
        model.addAttribute("user", user);
        return "user_profile";
    }

}
