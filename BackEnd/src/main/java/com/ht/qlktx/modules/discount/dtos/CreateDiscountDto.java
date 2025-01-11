package com.ht.qlktx.modules.discount.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class CreateDiscountDto {
    @NotEmpty(message = "Mã giảm giá không được để trống")
    private String id;

    @NotEmpty(message = "Nội dung không được để trống")
    private String description;

    @NotNull(message = "Phần trăm không được để trống")
    private BigDecimal percentage;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @JsonProperty("start_date")
    private Date startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @JsonProperty("end_date")
    private Date endDate;
}
