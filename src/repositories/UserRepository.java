package repositories;

import entities.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findById(int id);

    boolean updatePassword(int userId, String newPassword);

    boolean updateProfile(int userId, String newName, String newEmail);
}
