package com.example.scheduling.models;


import com.example.scheduling.enums.ServiceStatus;
import com.example.scheduling.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "services")
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;


    @Enumerated(EnumType.STRING)
    private ServiceType type; // enum with values: SERVICE, PRODUCT, PACKAGE

    @Enumerated(EnumType.STRING)
    private ServiceStatus status; // enum with values: ACTIVE, INACTIVE

    //created_at
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp created_at;

    //updated_at
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Timestamp updated_at;

    //tostring
    @Override
    public String toString() {
        return "Services{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", businesses=" + business +
                ", type=" + type +
                ", status=" + status +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }



}
