package ar.uba.fi.ingsoft1.user;

public record AdminDTO(
        long id,
        String username,
        String email,
        String password,
        String role
) implements AsUserInterface {
    public AdminDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), null, user.getRole());
    }

    public AdminDTO(String email, String username, String password) {
        this(0, username, email,  password, "admin");
    }

    public User asUser() {
        User user = new User(this.username, this.email, "admin");

        return user;
    }
}


