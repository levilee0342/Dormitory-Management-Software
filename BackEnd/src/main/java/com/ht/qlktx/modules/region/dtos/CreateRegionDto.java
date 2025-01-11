package com.ht.qlktx.modules.region.dtos;

import com.ht.qlktx.enums.Sex;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRegionDto {
    @NotEmpty(message = "Mã dãy không được để trống")
    private String id;

    @NotEmpty(message = "Tên dãy không được để trống")
    private String name;

    @NotNull(message = "Giới tính không được để trống")
    private Sex sex;
}
