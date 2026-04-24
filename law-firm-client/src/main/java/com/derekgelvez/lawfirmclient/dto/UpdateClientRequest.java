package com.derekgelvez.lawfirmclient.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UpdateClientRequest {
    private String phone;
    private String address;
}
