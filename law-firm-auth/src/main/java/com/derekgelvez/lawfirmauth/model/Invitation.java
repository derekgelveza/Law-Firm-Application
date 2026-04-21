package com.derekgelvez.lawfirmauth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String createdBy;
    private LocalDateTime expiresAt;
    private boolean used;




}
