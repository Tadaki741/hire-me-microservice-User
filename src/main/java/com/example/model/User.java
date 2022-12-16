package com.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity(name = "user")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column
    private String email;
    @Column
    private Boolean isRecruiter;


    public User() {}

    public User(String email, Boolean isRecruiter) {
        this.email = email;
        this.isRecruiter = isRecruiter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
