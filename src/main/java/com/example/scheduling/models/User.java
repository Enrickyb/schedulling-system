package com.example.scheduling.models;

import com.example.scheduling.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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

    //created_at
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    //updated_at
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updated_at;



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
