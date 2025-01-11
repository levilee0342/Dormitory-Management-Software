package com.ht.qlktx.modules.student.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.qlktx.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateProfileDto {
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private Sex sex;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    private String address;

    private String phone;
}
