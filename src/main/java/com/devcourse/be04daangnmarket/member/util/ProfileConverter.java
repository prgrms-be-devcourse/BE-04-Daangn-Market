package com.devcourse.be04daangnmarket.member.util;

import com.devcourse.be04daangnmarket.member.domain.Profile;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;

public class ProfileConverter {
    public static ProfileDto.Response toResponse(Profile profile) {
        return new ProfileDto.Response(
                profile.getMemberId(),
                profile.getUsername(),
                profile.getRegion(),
                profile.getTemperature(),
                profile.getCreatedAt()
        );
    }
}
