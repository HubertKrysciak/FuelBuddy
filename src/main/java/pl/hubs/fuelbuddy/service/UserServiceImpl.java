package pl.hubs.fuelbuddy.service;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.hubs.fuelbuddy.entity.User;
import pl.hubs.fuelbuddy.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    // private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        // Walidacja danych użytkownika
        validateUser(user);

        // Sprawdzenie, czy nazwa użytkownika jest unikalna
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Nazwa użytkownika jest już zajęta.");
        }

        // Haszowanie hasła (jeśli używasz PasswordEncoder)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Zapis użytkownika w bazie danych
        return userRepository.save(user);
    }

    private void validateUser(User user) {
        if (!StringUtils.hasText(user.getUsername())) {
            throw new IllegalArgumentException("Nazwa użytkownika jest wymagana.");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new IllegalArgumentException("Hasło jest wymagane.");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Nieprawidłowy adres e-mail.");
        }
    }

    private boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        // Prosty regex do walidacji e-maila
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        // Sprawdzenie, czy użytkownik istnieje
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Użytkownik o podanym ID nie istnieje.");
        }
        userRepository.deleteById(id);
    }
}
