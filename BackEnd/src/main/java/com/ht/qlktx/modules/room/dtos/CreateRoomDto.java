package com.ht.qlktx.modules.room.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateRoomDto {
    @NotEmpty(message = "Mã phòng không được để trống")
    private String id;

    @NotEmpty(message = "Mã dãy không được để trống")
    @JsonProperty("region_id")
    private String regionId;

    @NotNull(message = "Mã loại phòng không được để trống")
    @JsonProperty("type_id")
    private Long typeId;
}
