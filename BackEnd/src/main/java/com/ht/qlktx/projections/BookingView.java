package com.ht.qlktx.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.qlktx.entities.*;

import java.util.Date;

public interface BookingView {
    Long getId();

    @JsonProperty("created_at")
    Date getCreatedAt();

    @JsonProperty("booking_time")
    BookingTime getBookingTime();

    @JsonProperty("checkin_staff")
    Staff getCheckinStaff();

    RoomView getRoom();

    Student getStudent();

    @JsonProperty("checkout_staff")
    Staff getCheckoutStaff();

    @JsonProperty("checked_out_at")
    Date getCheckedOutAt();

    Discount getDiscount();
}

