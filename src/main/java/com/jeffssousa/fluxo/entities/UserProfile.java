package com.jeffssousa.fluxo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID profileId;

    private String name;
    private String lastName;
    private LocalDate birthDate;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
