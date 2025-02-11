package com.example.scheduling.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "businesses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private BusinessSettings settings;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, unique = true)
    private String name;


    private String description;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Services> services = new HashSet<>();

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
        return "Business{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
