package ar.uba.fi.ingsoft1.authorization;
import ar.uba.fi.ingsoft1.user.UserService;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.uba.fi.ingsoft1.user.AdminDTO;

@Component
public class AdminBootstrapper {
    @Autowired
    private UserService userService;

    @Value("${app.defaultAdmin.username}")
    private String defaultAdminUsername;

    @Value("${app.defaultAdmin.password}")
    private String defaultAdminPassword;

    @PostConstruct
    public void createDefaultAdmin() {
        if (!userService.atLeastOneAdminExists()) {
            AdminDTO admin = new AdminDTO(defaultAdminUsername, defaultAdminUsername, defaultAdminPassword);
            
            userService.createAdmin(admin);
        }
    }
}