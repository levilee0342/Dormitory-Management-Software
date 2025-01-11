package com.ht.qlktx.projections;

import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.enums.RoomStatus;

public interface RoomView {
    String getId();

    RoomType getType();

    RoomStatus getStatus();
}
