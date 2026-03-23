package com.example.elearning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @Column(name = "lecturer", length = 100)
    private String lecturer;

    @Column(name = "category_id")
    private Integer categoryId;

    // ==================== Constructors ====================
    public Course() {
    }

    public Course(String name, String image, Integer credits, String lecturer, Integer categoryId) {
        this.name = name;
        this.image = image;
        this.credits = credits;
        this.lecturer = lecturer;
        this.categoryId = categoryId;
    }

    // ==================== Getters & Setters ====================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
