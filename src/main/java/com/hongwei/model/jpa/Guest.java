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

    @Lob
    @Column(nullable = false)
    private String preference_json;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPreference_json() {
        return preference_json;
    }

    public void setPreference_json(String preference_json) {
        this.preference_json = preference_json;
    }
}
