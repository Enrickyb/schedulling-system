package com.example.scheduling.models;

import com.example.scheduling.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "guest_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String address;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business createdBy;

    //created_at
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime created_at;

    //updated_at
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updated_at;



    @Override
    public String toString() {
        return "GuestUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", created_by=" + createdBy +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
