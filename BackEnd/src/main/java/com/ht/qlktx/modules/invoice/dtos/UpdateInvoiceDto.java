package com.ht.qlktx.modules.invoice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class UpdateInvoiceDto {
    @JsonProperty("paid_at")
    private Date paidAt;

    private BigDecimal total;
}
