package ar.uba.fi.ingsoft1.user;

public record UserDTO(
        long id,
        String username,
        String email,
        String password,
        String apellido,
        Integer edad,
        String genero,
        String domicilio,
        String foto,
        String role

) implements AsUserInterface {
    public UserDTO(User user) {
        this(user.getId(), user.getUsername(), user.getEmail(), null, user.getApellido(), user.getEdad(), user.getGenero(), user.getDomicilio(), user.getFoto(), user.getRole());
    }

    public User asUser() {
        User user = new User(this.username, this.email, this.edad, this.genero, this.domicilio, this.foto, "user");
        user.setApellido(this.apellido);
        user.setEdad(this.edad);
        user.setGenero(this.genero);
        user.setDomicilio(this.domicilio);
        user.setFoto(this.foto);
        return user;
    }
}


