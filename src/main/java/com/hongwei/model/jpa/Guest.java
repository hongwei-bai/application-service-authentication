package com.hongwei.model.jpa;

import javax.persistence.*;

@Entity
public class Guest {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long expire_time;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String guest_code;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String preferred_language;
    
    @Lob
    @Column(nullable = false)
    private String roles;

    public Guest() {
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

    public String getGuest_code() {
        return guest_code;
    }

    public void setGuest_code(String guest_code) {
        this.guest_code = guest_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
