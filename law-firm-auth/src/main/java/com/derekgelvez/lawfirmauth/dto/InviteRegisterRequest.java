package com.derekgelvez.lawfirmauth.dto;

import com.derekgelvez.lawfirmauth.model.Invitation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InviteRegisterRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String inviteToken;
}
