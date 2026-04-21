package com.derekgelvez.lawfirmauth.service;

import com.derekgelvez.lawfirmauth.dto.InviteRegisterRequest;
import com.derekgelvez.lawfirmauth.dto.LoginRequest;
import com.derekgelvez.lawfirmauth.model.Invitation;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import com.derekgelvez.lawfirmauth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final InvitationService invitationService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(LoginRequest request) {
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            Users user = (Users) authentication.getPrincipal();
            return jwtUtil.generateToken(user);
        }

        throw new RuntimeException("Authentication failed");
    }

    public Users registerWithInvite(InviteRegisterRequest request) {
        Invitation invitation = invitationService.validateAndConsumeInvite(request.getInviteToken());

        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(invitation.getEmail());
        user.setRole(invitation.getRole());
        user.setPassword(encoder.encode(request.getPassword()));

        return repo.save(user);
    }
}