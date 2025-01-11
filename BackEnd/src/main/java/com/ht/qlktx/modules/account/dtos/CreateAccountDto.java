package com.ht.qlktx.modules.account.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class CreateAccountDto {
    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotEmpty(message = "Mật khẩu không được để trống")
    @Length(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;

    @NotEmpty(message = "Vai trò không được để trống")
    private String role;

    @NotEmpty(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotEmpty(message = "ID người dùng không được để trống")
    @JsonProperty("user_id")
    private String userId;
}
