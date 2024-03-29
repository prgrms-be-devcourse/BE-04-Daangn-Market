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

    public void create(Long memberId, String username, String region) {
        Profile profile = new Profile(memberId, username, region);

        profileRepository.save(profile);
    }

    public void create(Long memberId) {
        Profile profile = new Profile(memberId);

        profileRepository.save(profile);
    }

    public ProfileDto.Response update(Long id, String username) {
        Profile profile = getOne(id);

        if (isAvailableUsername(username)) {
            profile.updateProfile(username);

            return ProfileConverter.toResponse(profile);
        }

        throw new IllegalArgumentException(DUPLICATED_USERNAME.getMessage());
    }

    public ProfileDto.Response toProfile(Long id) {
        Profile profile = getOne(id);

        return ProfileConverter.toResponse(profile);
    }

    public Profile getOne(Long memberId) {
        return profileRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_PROFILE.getMessage()));
    }

    private boolean isAvailableUsername(String username) {
        return profileRepository.findByUsername(username).isEmpty();
    }
}
