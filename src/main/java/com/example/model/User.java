package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private Boolean isRecruiter;
    @Column
    private String customer;

    public User() {}

    public User(String customer) {
        this.customer = customer;
    }

    public User(String email, Boolean isRecruiter) {
        this.email = email;
        this.isRecruiter = isRecruiter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRecruiter() {
        return isRecruiter;
    }

    public void setRecruiter(Boolean recruiter) {
        isRecruiter = recruiter;
    }
}
