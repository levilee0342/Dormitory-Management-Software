package com.ht.qlktx.modules.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse<T> {
    private Credentials credentials;
    private T user;
}

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
class Credentials {
    @JsonProperty("access_token")
    private String accessToken;
}