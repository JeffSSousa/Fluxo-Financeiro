package com.jeffssousa.fluxo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tb_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    List<String> roles;
}
