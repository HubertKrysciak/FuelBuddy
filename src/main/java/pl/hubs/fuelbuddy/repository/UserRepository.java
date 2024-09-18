package pl.hubs.fuelbuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.hubs.fuelbuddy.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
