package com.ht.qlktx.modules.booking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookingDto {
    @NotNull(message = "Mã thời gian thuê không được để trống")
    @JsonProperty("booking_time_id")
    private Long bookingTimeId;

    @NotNull(message = "Mã phòng không được để trống")
    @JsonProperty("room_id")
    private String roomId;

    @NotNull(message = "Mã sinh viên không được để trống")
    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("discount_id")
    private String discountId;

    @JsonProperty("auto_create_invoice")
    private boolean autoCreateInvoice;
}
