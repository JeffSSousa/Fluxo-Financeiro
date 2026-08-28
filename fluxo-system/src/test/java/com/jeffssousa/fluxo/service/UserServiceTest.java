package com.jeffssousa.fluxo.service;

import com.jeffssousa.fluxo.builders.UserProfileTestBuilder;
import com.jeffssousa.fluxo.builders.UserTestBuilder;
import com.jeffssousa.fluxo.dto.user.AlterPasswordDTO;
import com.jeffssousa.fluxo.dto.user.UserCreateDTO;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.entities.UserProfile;
import com.jeffssousa.fluxo.exception.business.EmailAlreadyExistsException;
import com.jeffssousa.fluxo.exception.business.InvalidPasswordException;
import com.jeffssousa.fluxo.exception.business.PasswordMismatchException;
import com.jeffssousa.fluxo.mapper.UserProfileMapper;
import com.jeffssousa.fluxo.repository.UserProfileRepository;
import com.jeffssousa.fluxo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private CurrentUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private UserProfileMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Captor
    private ArgumentCaptor<UserProfile> profileCaptor;

    @InjectMocks
    private UserService service;

    @Nested
    class register{

        @Test
        @DisplayName("Deve cadastrar um novo usuario com sucesso")
        void shouldRegisterAUserWithSuccess(){

            String encryptedPassword = "Senha-criptografada";

            UserCreateDTO dto = new UserCreateDTO(
                    "email@mail.com",
                    "1234567",
                    "Victoria",
                    "Vieira",
                    LocalDate.of(2002,9,10)
            );

            UserProfile profile = UserProfileTestBuilder.aUserProfile()
                    .withName(dto.name())
                    .withLastName(dto.lastName())
                    .withBirthDate(dto.birthDate())
                    .build();

            when(userRepository.findByEmail(dto.email())).thenReturn(null);
            when(userRepository.save(any(User.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(mapper.toEntity(dto)).thenReturn(profile);
            when(profileRepository.save(any(UserProfile.class))).thenAnswer(
                    invocation -> invocation.getArgument(0));
            when(encoder.encode(dto.password())).thenReturn(encryptedPassword);

            service.register(dto);

            verify(userRepository, times(1)).findByEmail(dto.email());
            verify(encoder, times(1)).encode(dto.password());
            verify(userRepository, times(1)).save(userCaptor.capture());
            verify(mapper, times(1)).toEntity(dto);
            verify(profileRepository, times(1)).save(profileCaptor.capture());

            User returnedUser = userCaptor.getValue();
            UserProfile returnedProfile = profileCaptor.getValue();

            assertNotNull(returnedUser);
            assertNotNull(returnedProfile);

            assertEquals(dto.email(), returnedUser.getEmail());
            assertEquals(encryptedPassword, returnedUser.getPassword());
            assertEquals(1,returnedUser.getRoles().size());

            assertEquals(dto.name(), returnedProfile.getName());
            assertEquals(dto.lastName(), returnedProfile.getLastName());
            assertEquals(dto.birthDate(), returnedProfile.getBirthDate());

        }

        @Test
        @DisplayName("Deve lançar uma exceção quando o email já existir no banco")
        void shouldThrowExceptionWhenEmailAlreadyExists(){

            String email = "email@mail.com";

            User user = UserTestBuilder.aUser()
                    .withEmail(email)
                    .build();

            UserCreateDTO dto = new UserCreateDTO(
                    email,
                    "1234567",
                    "Victoria",
                    "Vieira",
                    LocalDate.of(2002,9,10)
            );


            when(userRepository.findByEmail(dto.email())).thenReturn(user);

            EmailAlreadyExistsException e = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> service.register(dto)
            );

            verify(userRepository, times(1)).findByEmail(dto.email());
            verify(encoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
            verify(mapper, never()).toEntity(dto);
            verify(profileRepository, never()).save(any(UserProfile.class));

            assertEquals("Este e-mail já está vinculado a uma conta", e.getMessage());

        }

    }

    @Nested
    class getByEmail{

        @Test
        @DisplayName("Deve buscar o usuario pelo email com sucesso")
        void shouldFindUserByEmailWithSuccess(){

            String email = "email@mail.com";

            User user = UserTestBuilder.aUser()
                    .withEmail(email)
                    .withUserId(UUID.randomUUID())
                    .build();

            when(userRepository.findByEmail(email)).thenReturn(user);

            User returnedUser = service.getByEmail(email);

            verify(userRepository, times(1)).findByEmail(email);

            assertNotNull(returnedUser);
            assertEquals(email, returnedUser.getEmail());

        }

    }

    @Nested
    class alterPassword{

        @Test
        @DisplayName("Deve Alterar a senha de um usuario")
        void shouldAlterPasswordWithSuccess(){

            String encryptedPassword = "Senha-criptografada";

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .withPassword(encryptedPassword)
                    .build();

            AlterPasswordDTO dto = new AlterPasswordDTO(
                    "123456",
                    "1234567",
                    "1234567"
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(encoder.matches(dto.currentPassword(),user.getPassword())).thenReturn(true);
            when(encoder.encode(dto.newPassword())).thenReturn(encryptedPassword);
            when(userRepository.save(any(User.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            String response = service.alterPassword(dto);

            verify(userService, times(1)).getAuthenticatedUser();
            verify(encoder, times(1))
                    .matches(dto.currentPassword(),user.getPassword());
            verify(encoder, times(1)).encode(dto.newPassword());
            verify(userRepository, times(1)).save(userCaptor.capture());

            User returnedUser = userCaptor.getValue();

            assertEquals("Senha alterada com sucesso", response);
            assertEquals(user.getUserId(), returnedUser.getUserId());
            assertEquals(encryptedPassword, returnedUser.getPassword());

        }

        @Test
        @DisplayName("Deve lançar uma exceção quando a senha atual estiver incorreta")
        void shouldThrowExceptionWhenIncorrectPassword(){

            String encryptedPassword = "Senha-criptografada";

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .withPassword(encryptedPassword)
                    .build();

            AlterPasswordDTO dto = new AlterPasswordDTO(
                    "123456",
                    "1234567",
                    "1234567"
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(encoder.matches(dto.currentPassword(),user.getPassword())).thenReturn(false);

            InvalidPasswordException e = assertThrows(
                    InvalidPasswordException.class,
                    () -> service.alterPassword(dto)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(encoder, times(1))
                    .matches(dto.currentPassword(),user.getPassword());
            verify(encoder, never()).encode(dto.newPassword());
            verify(userRepository, never()).save(any(User.class));

            assertEquals("Senha invalida!", e.getMessage());


        }

        @Test
        @DisplayName("Deve lançar uma exceção quando as senha não coincidem")
        void shouldThrowExceptionWhenPasswordsDoNotMatch(){

            String encryptedPassword = "Senha-criptografada";

            User user = UserTestBuilder.aUser()
                    .withUserId(UUID.randomUUID())
                    .withPassword(encryptedPassword)
                    .build();

            AlterPasswordDTO dto = new AlterPasswordDTO(
                    "123456",
                    "12345678",
                    "1234567"
            );

            when(userService.getAuthenticatedUser()).thenReturn(user);
            when(encoder.matches(dto.currentPassword(),user.getPassword())).thenReturn(true);

            PasswordMismatchException e = assertThrows(
                    PasswordMismatchException.class,
                    () -> service.alterPassword(dto)
            );

            verify(userService, times(1)).getAuthenticatedUser();
            verify(encoder, times(1))
                    .matches(dto.currentPassword(),user.getPassword());
            verify(encoder, never()).encode(dto.newPassword());


            assertEquals("As senhas não coincidem", e.getMessage());

        }

    }
}
