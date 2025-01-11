package com.ht.qlktx.modules.statistic.dtos;

import com.ht.qlktx.projections.RoomTypeWithRoomCountView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class StatisticOverview {
    private Long totalRooms;
    private Long totalRegions;
    private Long totalMaintainingRooms;
    private Long totalEmptyRooms;
    private Long totalStudents;
    private Long totalBookedStudents;
    private Long totalInvoices;
    private Long totalUnpaidInvoices;
    private List<RoomTypeWithRoomCountView> roomTypeStatistics;
}
