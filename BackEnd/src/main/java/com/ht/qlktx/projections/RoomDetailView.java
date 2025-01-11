package com.ht.qlktx.projections;

import com.ht.qlktx.entities.Region;
import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.enums.RoomStatus;

public interface RoomDetailView {
    String getId();

    RoomType getType();

    Region getRegion();

    RoomStatus getStatus();
}
