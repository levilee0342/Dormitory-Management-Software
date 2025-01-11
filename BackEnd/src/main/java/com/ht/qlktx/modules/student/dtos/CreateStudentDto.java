package com.ht.qlktx.modules.student.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.enums.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Getter
@Setter
public class CreateStudentDto {
    @NotEmpty(message = "Mã người dùng không được để trống")
    private String id;

    @NotEmpty(message = "Tên không được để trống")
    @JsonProperty("first_name")
    private String firstName;

    @NotEmpty(message = "Họ không được để trống")
    @JsonProperty("last_name")
    private String lastName;

    @NotNull(message = "Giới tính không được để trống")
    private Sex sex;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    private String address;

    private String phone;
}
