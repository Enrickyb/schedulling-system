package com.example.scheduling.models;

import com.example.scheduling.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String email;

    private String name;

    private String passwordHash;

    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private UserRole role;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Aqui você pode personalizar a lógica para retornar os papéis do usuário
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;  // Retorne a senha hashada
    }

    @Override
    public String getUsername() {
        return email;  // O nome de usuário é o e-mail
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Você pode adicionar lógica de expiração de conta, se necessário
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Você pode adicionar lógica de bloqueio de conta, se necessário
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Você pode adicionar lógica de expiração de credenciais, se necessário
    }

    @Override
    public boolean isEnabled() {
        return true;  // Você pode adicionar lógica de habilitação de conta, se necessário
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                '}';
    }
}
