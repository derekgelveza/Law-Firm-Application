package com.derekgelvez.lawfirmauth.repository;

import com.derekgelvez.lawfirmauth.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByToken(String token);

    boolean existsByEmailAndUsed(String email, boolean used);

}
