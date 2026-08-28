package com.jeffssousa.fluxo.repository;

import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    UserProfile findByUser(User user);

}
