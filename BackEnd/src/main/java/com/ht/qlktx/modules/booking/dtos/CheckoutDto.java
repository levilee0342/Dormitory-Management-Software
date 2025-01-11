package com.ht.qlktx.modules.booking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Date;

@Getter
public class CheckoutDto {
    @NotNull(message = "Ngày trả phòng không được để trống")
    @JsonProperty("checkout_at")
    private Date checkoutAt;
}
