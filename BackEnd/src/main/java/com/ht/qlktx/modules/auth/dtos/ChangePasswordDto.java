package com.ht.qlktx.modules.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChangePasswordDto {
    @NotEmpty(message = "Mật khẩu hiện tại không được để trống")
    @Length(min = 6, message = "Mật khẩu hiện tại phải có ít nhất 6 ký tự")
    @JsonProperty("current_password")
    private String currentPassword;

    @NotEmpty(message = "Mật khẩu mới không được để trống")
    @Length(min = 6, message = "Mật khẩu mới phải có ít nhất 6 ký tự")
    @JsonProperty("new_password")
    private String newPassword;
}
