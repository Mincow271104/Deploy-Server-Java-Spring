package com.example.elearning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    // ==================== Constructors ====================
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    // ==================== Getters & Setters ====================
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
