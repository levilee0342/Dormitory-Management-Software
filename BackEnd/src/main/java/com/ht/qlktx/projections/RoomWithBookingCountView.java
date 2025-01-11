package com.ht.qlktx.projections;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.qlktx.entities.Region;
import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.enums.RoomStatus;

public interface RoomWithBookingCountView {
    String getId();

    RoomType getType();

    Region getRegion();

    RoomStatus getStatus();

    @JsonProperty("booking_count")
    Long getBookingCount();
}
