package com.derekgelvez.lawfirmauth.service;

import com.derekgelvez.lawfirmauth.dto.CreateInviteRequest;
import com.derekgelvez.lawfirmauth.model.Invitation;
import com.derekgelvez.lawfirmauth.model.Role;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.repository.InvitationRepository;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import com.derekgelvez.lawfirmcommon.exception.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;


    //creates a new invitation
    public Invitation createInviteRequest(CreateInviteRequest request, String createdByEmail)
            throws UsernameNotFoundException {

        Users invite = userRepository.findByEmail(createdByEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with that email: "+ createdByEmail));

        Role InviterRole = invite.getRole();
        Role inviteeRole = request.getRole();

        switch (InviterRole){
            case SUPER_ADMIN:
                break;

            case ATTORNEY:
                if (inviteeRole != Role.CLIENT && inviteeRole != Role.CLERK){
                    throw new RuntimeException("Attorneys can only invite CLIENT or CLERK");
                }
                break;

            case CLERK:
                if (inviteeRole != Role.CLIENT){
                    throw new RuntimeException("Clerks can only invite CLIENT");
                }
                break;

            default:
                throw new RuntimeException("You do not have permission to send invitations");
        }

        if (invitationRepository.existsByEmailAndUsed(request.getEmail(), false)){
            throw new RuntimeException("An active invitation already exists for: " + request.getEmail());
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(request.getEmail());
        invitation.setRole(request.getRole());
        invitation.setCreatedBy(createdByEmail);
        invitation.setToken(UUID.randomUUID().toString());
        invitation.setExpiresAt(LocalDateTime.now().plusHours(48));
        invitation.setUsed(false);

        return invitationRepository.save(invitation);
    }

    public Invitation validateAndConsumeInvite(String token){

        Invitation tokenUsed = invitationRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Not a valid invite number"));

        if (tokenUsed.isUsed()){
            throw new RuntimeException("This invitation has been used");
        }
        if (tokenUsed.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("This invitation has expired");
        }

        tokenUsed.setUsed(true);
        invitationRepository.save(tokenUsed);

        return tokenUsed;


    }

}
