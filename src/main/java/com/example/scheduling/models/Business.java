package com.example.scheduling.models;

import jakarta.persistence.*;
import lombok.*;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.UUID;

@Entity
@Table(name = "businesses")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", owner=" + owner +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
