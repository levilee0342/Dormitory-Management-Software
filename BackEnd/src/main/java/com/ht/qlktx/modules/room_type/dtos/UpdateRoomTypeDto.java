package com.ht.qlktx.modules.room_type.dtos;

import com.ht.qlktx.enums.Sex;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateRoomTypeDto {
    private String name;

    private int capacity;

    private BigDecimal price;
}
