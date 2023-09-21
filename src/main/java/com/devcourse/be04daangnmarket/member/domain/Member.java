package com.devcourse.be04daangnmarket.member.domain;

import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ALIVE;

    protected Member() {
    }

    public Member(String phoneNumber, String email, String password) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public Member(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isMatchedPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }
}
