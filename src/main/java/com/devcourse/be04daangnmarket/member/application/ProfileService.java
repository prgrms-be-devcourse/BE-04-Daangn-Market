package com.devcourse.be04daangnmarket.member.application;

import com.devcourse.be04daangnmarket.member.domain.Profile;
import com.devcourse.be04daangnmarket.member.dto.ProfileDto;
import com.devcourse.be04daangnmarket.member.repository.ProfileRepository;
import com.devcourse.be04daangnmarket.member.util.ProfileConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.DUPLICATED_USERNAME;
import static com.devcourse.be04daangnmarket.member.exception.ErrorMessage.NOT_FOUND_PROFILE;

@Service
@Transactional
public class ProfileService {
    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDto.Response updateProfile(Long id, String username) {
        Profile profile = getProfile(id);

        if (isAvailableUsername(username)) {
            profile.updateProfile(username);

            return ProfileConverter.toResponse(profile);
        }

        throw new IllegalArgumentException(DUPLICATED_USERNAME.getMessage());
    }

    public Profile getProfile(Long memberId) {
        return profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_PROFILE.getMessage()));
    }

    public ProfileDto.Response toProfile(Long id) {
        Profile profile = getProfile(id);

        return ProfileConverter.toResponse(profile);
    }

    private boolean isAvailableUsername(String username) {
        return profileRepository.findByUsername(username).isEmpty();
    }
}
