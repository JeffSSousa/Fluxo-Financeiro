package com.jeffssousa.fluxo.mapper;

import com.jeffssousa.fluxo.dto.user.ProfileResponseDTO;
import com.jeffssousa.fluxo.dto.user.UserCreateDTO;
import com.jeffssousa.fluxo.entities.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfile toEntity(UserCreateDTO dto);

    ProfileResponseDTO toDto(UserProfile profile);
}
