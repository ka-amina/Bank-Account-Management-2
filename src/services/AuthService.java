package services;

import entities.User;
import repositories.UserRepository;
import repositories.UserRepositoryImpl;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public Optional<User> login(String email, String password) {
        Optional<User> findUser = userRepository.findByEmail(email);
        if (findUser.isPresent()) {
            User user = findUser.get();

            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean changePassword(int userId, String currPassword, String newPassword) {
        return userRepository.findById(userId)
                .filter(u -> u.getPassword().equals(currPassword))
                .map(u -> userRepository.updatePassword(userId, newPassword))
                .orElse(false);
    }
}
