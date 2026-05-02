package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.UserProfileTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.user.ProfileResponseDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.entities.UserProfile;
import com.jeffssousa.fluxo.mapper.UserProfileMapper;
import com.jeffssousa.fluxo.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @Mock
    private CurrentUserService userService;

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private UserProfileMapper mapper;

    @InjectMocks
    private UserProfileService userProfileService;

    @Nested
    class getProfile{

        @Test
        @DisplayName("Deve retornar os detalhes do usuario com sucesso")
        void shouldReturnUserProfileSuccessfully(){

            User user = UserTestBuilder.aUser().build();
            UserProfile userProfile = UserProfileTestBuilder.aUserProfile()
                    .withProfileId(UUID.randomUUID())
                    .withUser(user)
                    .build();

            ProfileResponseDTO dto = new ProfileResponseDTO(
                    userProfile.getName(),
                    userProfile.getLastName(),
                    userProfile.getBirthDate()
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(profileRepository.findByUser(user)).thenReturn(userProfile);
            when(mapper.toDto(userProfile)).thenReturn(dto);

            ProfileResponseDTO response = userProfileService.getProfile();

            verify(userService, times(1)).getAuthenticatedUser();
            verify(profileRepository, times(1)).findByUser(user);
            verify(mapper, times(1)).toDto(userProfile);

            assertNotNull(response);
            assertEquals(dto.name(), response.name());
            assertEquals(dto.lastName(), response.lastName());
            assertEquals(dto.birthDate(), response.birthDate());




        }

        @Test
        @DisplayName("Deve lançar uma exceção caso os detalhes não sejam encontrado")
        void shouldThrowExceptionWhenUserProfileNotFound(){

            User user = UserTestBuilder.aUser().build();

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(profileRepository.findByUser(user)).thenReturn(null);

            EntityNotFoundException e = assertThrows(
                    EntityNotFoundException.class,
                    () -> userProfileService.getProfile()
                    );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(profileRepository, times(1)).findByUser(user);
            verify(mapper, times(0)).toDto(any());

            assertEquals("Perfil não encontrado", e.getMessage());
        }

    }

}
