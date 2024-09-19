package pl.hubs.fuelbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.hubs.fuelbuddy.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
