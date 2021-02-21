package com.khpi.farmacy.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtAuthenticationResponseDto implements Dto {

    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
