package com.jeffssousa.fluxo.builders;

import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.entities.UserProfile;

import java.time.LocalDate;
import java.util.UUID;

public class UserProfileTestBuilder {

    private UUID profileId;
    private String name = "Jefferson";
    private String lastName = "Silva";
    private LocalDate birthDate = LocalDate.of(1995, 1, 1);

    private User user;

    private UserProfileTestBuilder() {}

    public static UserProfileTestBuilder aUserProfile() {
        return new UserProfileTestBuilder();
    }

    public UserProfileTestBuilder withProfileId(UUID profileId) {
        this.profileId = profileId;
        return this;
    }

    public UserProfileTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserProfileTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserProfileTestBuilder withBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public UserProfileTestBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public UserProfile build() {
        UserProfile profile = new UserProfile();

        profile.setProfileId(profileId);
        profile.setName(name);
        profile.setLastName(lastName);
        profile.setBirthDate(birthDate);
        profile.setUser(user);

        return profile;
    }
}