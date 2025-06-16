
package ar.uba.fi.ingsoft1.user;

import ar.uba.fi.ingsoft1.cart.*;

import ar.uba.fi.ingsoft1.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Optional<User> getUser(String username) {
        return userRepository.findByEmail(username);
    }

    private User getUser(AsUserInterface data) {
        Optional<User> existingUser = userRepository.findByEmail(data.email());
        if (existingUser.isPresent()) {
            if(existingUser.get().isActive()){
                throw new IllegalArgumentException("El correo electrónico ya está registrado.");
            }

            userRepository.delete(existingUser.get());
        }

        User user = data.asUser();

        var hasher = new BCryptPasswordEncoder();
        String passwordHash = hasher.encode(data.password());
        user.setPasswordHash(passwordHash);
        return user;
    }

    public UserDTO createUser(UserDTO data) {
        User user = getUser(data);

        Cart userCart = new Cart();
        userCart = cartRepository.save(userCart);
        user.setCart(userCart);
        
        return new UserDTO(userRepository.save(user));
    }

    public AdminDTO createAdmin(AdminDTO data) {
        User user = getUser(data);
        var hasher = new BCryptPasswordEncoder();
        String passwordHash = hasher.encode(data.password());
        user.setPasswordHash(passwordHash);
        user.setActive(true);
        return new AdminDTO(userRepository.save(user));
    }

    public String hashPassword(String pass) {
        var hasher = new BCryptPasswordEncoder();
        return hasher.encode(pass);
    }

    public boolean atLeastOneAdminExists() {
        return userRepository.countAdmins() > 0;
    }

    public void confirmUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("El token de confirmacion es invalido.")
        );

        user.setActive(true);
    }

    public void updateUserPassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("El token de confirmacion es invalido.")
        );

        var hasher = new BCryptPasswordEncoder();
        String passwordHash = hasher.encode(password);
        user.setPasswordHash(passwordHash);
    }
}
