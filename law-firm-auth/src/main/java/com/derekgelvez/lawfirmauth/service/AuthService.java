package com.derekgelvez.lawfirmauth.service;

import com.derekgelvez.lawfirmauth.dto.ChangePasswordRequest;
import com.derekgelvez.lawfirmauth.dto.InviteRegisterRequest;
import com.derekgelvez.lawfirmauth.dto.LoginRequest;
import com.derekgelvez.lawfirmauth.model.Invitation;
import com.derekgelvez.lawfirmauth.model.Users;
import com.derekgelvez.lawfirmauth.repository.UserRepository;
import com.derekgelvez.lawfirmauth.security.JwtUtil;
import com.derekgelvez.lawfirmcommon.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
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

    public void changePassword(ChangePasswordRequest request, String email) {

        // Load the user making the request
        Users user = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with email: " + email));

        // Verify their current password is correct
        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Make sure new password and confirm password match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        // Make sure they are not reusing the same password
        if (encoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password must be different from current password");
        }

        // Hash and save the new password
        user.setPassword(encoder.encode(request.getNewPassword()));
        repo.save(user);
    }
}