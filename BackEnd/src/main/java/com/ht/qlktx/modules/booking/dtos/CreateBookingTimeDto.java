package com.ht.qlktx.modules.booking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CreateBookingTimeDto {
    @NotEmpty(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @JsonProperty("start_date")
    private Date startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @JsonProperty("end_date")
    private Date endDate;

    @NotNull(message = "Trạng thái không được để trống")
    private boolean open;
}
