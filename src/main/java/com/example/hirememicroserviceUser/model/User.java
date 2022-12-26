package com.example.hirememicroserviceUser.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.io.Serializable;


@Entity
@Table(name = "users")
@Setter
@Getter
@ToString
public class User implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id",nullable = false)
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
