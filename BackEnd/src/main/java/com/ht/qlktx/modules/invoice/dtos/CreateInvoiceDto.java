package com.ht.qlktx.modules.invoice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CreateInvoiceDto {

    @NotNull(message = "Mã phiếu thuê không được để trống")
    @JsonProperty("booking_id")
    private Long bookingId;

    @NotNull(message = "Tổng tiền không được để trống")
    private BigDecimal total;
}
