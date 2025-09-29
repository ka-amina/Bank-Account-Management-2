package repositories;

import entities.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
}
