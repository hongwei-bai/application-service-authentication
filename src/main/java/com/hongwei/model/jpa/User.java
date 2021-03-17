package com.hongwei.model.jpa;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false)
    private String password_hash;

    @Column(nullable = false)
    private String role;

    @Lob
    @Column(nullable = false)
    private String preference_json;

    public User() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash.toLowerCase();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPreference_json() {
        return preference_json;
    }

    public void setPreference_json(String preference_json) {
        this.preference_json = preference_json;
    }
}