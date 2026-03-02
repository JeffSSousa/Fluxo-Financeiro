package com.jeffssousa.fluxo.service;


import com.jeffssousa.fluxo.dto.UserCreateDTO;
import com.jeffssousa.fluxo.entities.User;
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

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public void register(UserCreateDTO dto){

        log.info("Criando novo usuario");

        User user = createUserAdmin(dto);
        userRepository.save(user);

        log.info("Usuario criado com sucesso");
    }

    private User createUserAdmin(UserCreateDTO dto){
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        user.setRoles(List.of("ADMIN"));
        return user;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);

    }
}
