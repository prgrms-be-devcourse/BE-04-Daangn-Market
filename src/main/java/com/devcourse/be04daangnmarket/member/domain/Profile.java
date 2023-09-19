package com.devcourse.be04daangnmarket.member.domain;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    private Long memberId;

    @Column(unique = true, nullable = false)
    private String username;

    private String region;

    private double temperature = 36.5;

    public Profile(Long memberId, String username, String region) {
        this.memberId = memberId;
        this.username = username;
        this.region = region;
    }

    protected Profile() {

    }

    public Long getMemberId() {
        return memberId;
    }

    public String getUsername() {
        return username;
    }

    public String getRegion() {
        return region;
    }

    public double getTemperature() {
        return temperature;
    }

    public void updateProfile(String username) {
        this.username = username;
    }
}
