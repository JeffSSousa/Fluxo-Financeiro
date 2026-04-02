package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.dto.ProfileResponseDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.entities.UserProfile;
import com.jeffssousa.fluxo.mapper.UserProfileMapper;
import com.jeffssousa.fluxo.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final CurrentUserService userService;

    private final UserProfileRepository profileRepository;

    private final UserProfileMapper mapper;

    public ProfileResponseDTO getProfile() {

        User user = userService.getAuthenticatedUser();
        log.info("[READ] Profile - user: {}", user.getEmail());

        UserProfile profile = profileRepository.findByUser(user);

        if (profile == null){
            log.warn("[READ] Profile NOT FOUND - user: {}", user.getEmail());
            throw new EntityNotFoundException("Perfil não encontrado");
        }

        log.info("[READ SUCCESS] Profile - user: {}, profileId: {}", user.getEmail(), profile.getProfileId());

        return mapper.toDto(profile);

    }
}
