package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CurrentUserService userService;

    @Nested
    class getAuthenticateUser {

        @Test
        @DisplayName("Deve retornar o usuario autenticado com sucesso.")
        void shouldReturnAuthenticatedUser() {

            User user = UserTestBuilder.aUser().build();

            Authentication authentication = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);

            when(authentication.getName()).thenReturn(user.getEmail());
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

            User result = userService.getAuthenticatedUser();

            verify(userRepository, times(1)).findByEmail(user.getEmail());

            assertNotNull(result);
            assertEquals(user.getEmail(), result.getEmail());
        }


        }

}
