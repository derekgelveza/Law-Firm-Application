package com.derekgelvez.lawfirmauth.dto;

import com.derekgelvez.lawfirmauth.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateInviteRequest {
    private String email;
    private Role role;
}
