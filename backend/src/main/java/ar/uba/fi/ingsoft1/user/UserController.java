package ar.uba.fi.ingsoft1.user;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import ar.uba.fi.ingsoft1.exception.ErrorAtSendingEmailException;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import ar.uba.fi.ingsoft1.authorization.AuthorizationService;
import ar.uba.fi.ingsoft1.authorization.JwtHandler;
import ar.uba.fi.ingsoft1.authorization.RequiresPrivilege;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


@RestController
@CrossOrigin(origins = "http://localhost:3000") 
@RequiredArgsConstructor
class UserController {
    private final UserService userService;
    private final JwtHandler jwtHandler;
    private final AuthorizationService authorizationService;
    private final EmailService emailService;

    @Operation(summary = "login with existing user's credentials") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "successful login. Returns the generated JWToken"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
    })
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || authHeader.isBlank()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Missing authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
        String encodedCredentials = authHeader.substring("Basic ".length());
        byte[] decodedBytes = Base64.getDecoder().decode(encodedCredentials);
        String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
        String[] values = credentials.split(":", 2);
        String email = values[0];
        String decodedPassword = values[1];
        var hasher = new BCryptPasswordEncoder();

        User user = userService.getUser(email).orElse(null);

        if (user == null || !user.isActive()) {
            Map<String, String> errorResponse = new HashMap<>(); // TODO: Temporal para que el front no se rompa
            errorResponse.put("message", "Invalid credentials or unconfirmed account. Please try again.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        if (!hasher.matches(decodedPassword, user.getPasswordHash())) {
            Map<String, String> errorResponse = new HashMap<>(); // TODO: Temporal para que el front no se rompa
            errorResponse.put("message", "Invalid credentials. Please try again.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        Map<String, Object> responseBody = new HashMap<>(); // TODO: Temporal para que el front no se rompa
        responseBody.put("message", "Login successful");
        String jwt = jwtHandler.generateToken(email, user.getId(), user.getRole());

        responseBody.put("token", jwt);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(summary = "register a new user") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "user created"),
        @ApiResponse(responseCode = "409", description = "user with this email already exists"),
        @ApiResponse(responseCode = "503", description = "unexpected error when sending confirmation email")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO newUser = userService.createUser(userDTO);
            String confirmationToken = jwtHandler.generateToken(newUser.username(), newUser.id(), newUser.role());
            String confirmationLink = generateConfirmationLink(newUser.email(),
                    confirmationToken);
            emailService.sendConfirmationEmail("delivered@resend.dev", confirmationLink);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("user", newUser);
            responseBody.put("token", confirmationToken);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (ErrorAtSendingEmailException e) {
            return new ResponseEntity<>("The email confirmation service isn't working.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/confirm")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user confirmed"),
            @ApiResponse(responseCode = "400", description = "invalid confirmation token"),
    })
    public ResponseEntity<String> confirmEmail(@RequestBody Map<String, Object> request) {
        String token = (String) request.get("token");

        try {
            Claims claims = authorizationService.validateTokenAndExtractClaims(token);
            userService.confirmUser(authorizationService.getUserId(claims));
            return ResponseEntity.ok("Email confirmed successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid confirmation token.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> requestPasswordResset(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");

        try {
            User newUser = userService.getUser(email).orElse(null);

            if (newUser == null){
                return new ResponseEntity<>("Invalid Email.", HttpStatus.NOT_FOUND);
            }
            UserDTO userDTO = new UserDTO(newUser);
            
            String passwordToken = jwtHandler.generateToken(userDTO.username(), userDTO.id(), userDTO.role());
            String passwordLink = generatePasswordRecoverLink(userDTO.email(),
                        passwordToken);
            emailService.sendPasswordResetEmail("delivered@resend.dev", passwordLink);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("url", passwordLink);

            return ResponseEntity.ok(responseBody);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (ErrorAtSendingEmailException e) {
            return new ResponseEntity<>("The email confirmation service isn't working.", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/reset-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user password reseted"),
            @ApiResponse(responseCode = "400", description = "invalid reset password token"),
    })
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, Object> request) {
        String token = (String) request.get("token");
        String password = (String) request.get("new_password");

        try {
            Claims claims = authorizationService.validateTokenAndExtractClaims(token);
            userService.updateUserPassword(authorizationService.getUserId(claims), password);
            return ResponseEntity.ok("User password reseted successfully!");
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid reset password token.", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "register a new admin user") 
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "admin user registered"),
        @ApiResponse(responseCode = "401", ref = "#/components/responses/401"), 
        @ApiResponse(responseCode = "403", ref = "#/components/responses/403"),
        @ApiResponse(responseCode = "409", description = "a user with that email already exists")
    })
    @PostMapping("/admins")
    @RequiresPrivilege("admin")
    public ResponseEntity<Map<String, String>> registerAdmin(@RequestBody AdminDTO adminDTO) {
        Map<String, String> response = new HashMap<>();
        
        try {
            AdminDTO newUser = userService.createAdmin(adminDTO);
            response.put("message", "Admin created successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/privileges")
    @RequiresPrivilege("admin")
    public ResponseEntity<Map<String, String>> privileges() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Valid privileges");
        return ResponseEntity.ok(response);
    }

    private String generateConfirmationLink(String email, String token) {
        return "http://localhost:3000/confirm?token=" + token + "&email=" + email;
    }

    private String generatePasswordRecoverLink(String email, String token) {
        return "http://localhost:3000/reset?token=" + token + "&email=" + email;
    }
}