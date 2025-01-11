package com.ht.qlktx.modules.room_type.dtos;

import com.ht.qlktx.enums.Sex;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoomTypeDto {
    @NotEmpty(message = "Tên loại phòng không được để trống")
    private String name;

    @NotNull(message = "Sức chứa không được để trống")
    private int capacity;

    @NotNull(message = "Giá không được để trống")
    private BigDecimal price;
}
