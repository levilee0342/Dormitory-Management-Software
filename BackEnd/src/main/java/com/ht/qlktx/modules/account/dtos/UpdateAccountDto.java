package com.ht.qlktx.modules.account.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountDto {
    private String email;

    private String password;
}
