package ar.uba.fi.ingsoft1.user;

import ar.uba.fi.ingsoft1.cart.Cart;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {

        @Id
        @GeneratedValue
        private Long id;

        private String username;

        private String email;
        private String passwordHash;

        private String apellido;
        private Integer edad;
        private String genero;
        private String domicilio;
        private String foto;

        @OneToOne
        private Cart cart = null;

        private String role;
        private boolean isActive;

        public User(String username, String email, Integer edad, String genero, String domicilio, String foto, String role) {
                this.username = username;
                this.email = email;
                this.edad = edad;
                this.genero = genero;
                this.domicilio = domicilio;
                this.foto = foto;
                this.role = role;
                this.isActive = false;
        }

        public User(String username, String email, String role) {
                this.username = username;
                this.email = email;
                this.role = role;
                this.isActive = false;
        }

        public void setPasswordHash(String hash) {
                this.passwordHash = hash;
        }

        public void setCart(Cart cart){
            this.cart = cart;
        }
}
