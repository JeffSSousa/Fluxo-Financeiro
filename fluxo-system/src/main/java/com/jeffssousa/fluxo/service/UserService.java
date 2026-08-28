package com.jeffssousa.fluxo.service;


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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final CurrentUserService userService;

    private final UserRepository userRepository;

    private final UserProfileRepository profileRepository;

    private final UserProfileMapper mapper;

    private final PasswordEncoder encoder;

    public void register(UserCreateDTO dto){

        log.info("[CREATE] User - email: {}", dto.email());

        if (userRepository.findByEmail(dto.email()) != null){
            String msg = "Este e-mail já está vinculado a uma conta";
            log.warn("[CREATE] User DUPLICATED - email: {}", dto.email());
            throw new EmailAlreadyExistsException(msg);
        }

        User user = createUser(dto);
        userRepository.save(user);

        UserProfile userProfile = mapper.toEntity(dto);
        userProfile.setUser(user);
        profileRepository.save(userProfile);

        log.info("[CREATE SUCCESS] User - email: {}, userId: {}", dto.email(), user.getUserId());
    }

    private User createUser(UserCreateDTO dto){
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        user.setRoles(List.of("USER"));
        return user;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    public String alterPassword(AlterPasswordDTO dto) {

        User user = userService.getAuthenticatedUser();
        log.info("[UPDATE] Password - user: {}", user.getEmail());


        if (!encoder.matches(dto.currentPassword(), user.getPassword())){
            log.warn("[UPDATE] Password INVALID CURRENT - user: {}", user.getEmail());
            throw new InvalidPasswordException("Senha invalida!");
        }

        if (!dto.newPassword().equals(dto.confirmPassword())){
            log.warn("[UPDATE] Password MISMATCH - user: {}", user.getEmail());
            throw new PasswordMismatchException("As senhas não coincidem");
        }

        user.setPassword(encoder.encode(dto.newPassword()));
        userRepository.save(user);
        log.info("[UPDATE SUCCESS] Password - user: {}", user.getEmail());
        return "Senha alterada com sucesso";

    }
}
