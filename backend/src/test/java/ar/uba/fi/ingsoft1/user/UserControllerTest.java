package ar.uba.fi.ingsoft1.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Optional;

import ar.uba.fi.ingsoft1.authorization.AuthorizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ar.uba.fi.ingsoft1.authorization.JwtHandler;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private JwtHandler jwtHandler;

    @Test
    void testNoAuthHeaderShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Missing authorization header"));
    }

    @Test
    void testCorrectAuthHeaderShouldReturnJWToken() throws Exception {
        String email = "user@gmail.com";
        String password = "1234";
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((email + ":" + password).getBytes());
        User user = new User("user", email, 100, "masc", "calle 123", "foto", "user");
        user.setActive(true);
        var hasher = new BCryptPasswordEncoder();
        String hash = hasher.encode(password);
        user.setPasswordHash(hash);

        when(userService.getUser(email)).thenReturn(Optional.of(user));
        when(jwtHandler.generateToken(email, user.getId(), user.getRole())).thenReturn("tokenfalso");

        mockMvc.perform(post("/login")
                .header("Authorization", encodedAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.token").value("tokenfalso"));
    }
}
