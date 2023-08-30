package com.devcourse.be04daangnmarket.member.domain;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import com.devcourse.be04daangnmarket.member.domain.constant.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private double temperature = 36.5;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ALIVE;

    protected Member() {

    }

    public Member(String username, String phoneNumber, String email, String password) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public double getTemperature() {
        return temperature;
    }

    public Status getStatus() {
        return status;
    }
}
