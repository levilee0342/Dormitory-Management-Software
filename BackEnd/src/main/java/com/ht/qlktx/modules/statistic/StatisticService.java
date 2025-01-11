package com.ht.qlktx.modules.statistic;

import com.ht.qlktx.enums.RoomStatus;
import com.ht.qlktx.modules.invoice.InvoiceRepository;
import com.ht.qlktx.modules.region.RegionRepository;
import com.ht.qlktx.modules.room.RoomRepository;
import com.ht.qlktx.modules.room_type.RoomTypeRepository;
import com.ht.qlktx.modules.statistic.dtos.StatisticOverview;
import com.ht.qlktx.modules.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final RoomRepository roomRepository;
    private final RegionRepository regionRepository;
    private final InvoiceRepository invoiceRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final StudentRepository studentRepository;
    public StatisticOverview getOverview() {
        var totalRooms = roomRepository.countByDeletedIsFalse();
        var totalRegions = regionRepository.countByDeletedIsFalse();
        var totalStudents = studentRepository.countByDeletedIsFalse();
        var totalInvoices = invoiceRepository.countByDeletedIsFalse();
        var totalMaintainingRooms = roomRepository.countByDeletedIsFalseAndStatusIs(RoomStatus.MAINTAINING);
        var totalUnpaidInvoices = invoiceRepository.countByDeletedIsFalseAndPaidAtIsNull();
        var totalEmptyRooms = roomRepository.countEmptyRooms();
        var totalBookedStudents = studentRepository.countBookedStudents();
        var roomTypeStatistics = roomTypeRepository.findAllWithRoomCount();
        return StatisticOverview.builder()
                .totalRooms(totalRooms)
                .totalRegions(totalRegions)
                .totalMaintainingRooms(totalMaintainingRooms)
                .totalEmptyRooms(totalEmptyRooms)
                .totalStudents(totalStudents)
                .totalBookedStudents(totalBookedStudents)
                .totalInvoices(totalInvoices)
                .totalUnpaidInvoices(totalUnpaidInvoices)
                .roomTypeStatistics(roomTypeStatistics)
                .build();
    }
}
