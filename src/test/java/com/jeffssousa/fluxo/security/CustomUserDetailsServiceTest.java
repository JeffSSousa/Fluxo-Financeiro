package com.jeffssousa.fluxo.security;

import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailsService service;


    @Nested
    class loadUserByUsername{

        @Test
        @DisplayName("Deve retornar o UserDetails quando usuario existir")
        void shouldReturnUserDetailsWhenUserExists() {

            User user = UserTestBuilder.aUser()
                    .withEmail("email@mail.com")
                    .withPassword("senha")
                    .withRoles(List.of("USER"))
                    .build();

            when(userService.getByEmail(user.getEmail()))
                    .thenReturn(user);


            UserDetails result = service.loadUserByUsername(user.getEmail());

            verify(userService, times(1)).getByEmail(user.getEmail());

            assertNotNull(result);
            assertEquals(user.getEmail(), result.getUsername());
            assertEquals(user.getPassword(), result.getPassword());

            assertTrue(
                    result.getAuthorities()
                            .stream()
                            .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))
            );

        }


        @Test
        @DisplayName("Deve lançar exceção quando usuário não existir")
        void shouldThrowExceptionWhenUserDoesNotExist() {

            when(userService.getByEmail("email@mail.com"))
                    .thenReturn(null);

            UsernameNotFoundException e = assertThrows(
                    UsernameNotFoundException.class,
                    () -> service.loadUserByUsername("email@mail.com")
            );

            verify(userService).getByEmail("email@mail.com");

            assertEquals("Usuario não encontrado", e.getMessage());

        }


    }

}
