package com.prepify.be.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "name" trong DB tương ứng với "fullName" trong entity cho dễ hiểu
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "role", nullable = false, length = 10)
    private String role;

    @Column(name = "avatar")
    private String avatar;

    @JsonProperty("created_at")
    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @JsonProperty("updated_at")
    @Column(name = "updated_at")
    private String updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now().toString();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now().toString();
    }
}
