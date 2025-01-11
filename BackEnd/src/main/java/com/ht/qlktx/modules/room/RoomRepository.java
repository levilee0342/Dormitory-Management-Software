package com.ht.qlktx.modules.room;

import com.ht.qlktx.entities.Room;
import com.ht.qlktx.enums.RoomStatus;
import com.ht.qlktx.projections.RoomWithBookingCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByIdContainingIgnoreCaseAndDeletedFalse(String keyword);

    List<Room> findAllByDeletedFalse();

    @Query("""
        SELECT r.id as id, r.region as region, r.type as type, r.status as status, COUNT(b.id) as bookingCount FROM Room r
        LEFT JOIN r.type
        LEFT JOIN r.bookings b on b.room.id = r.id and b.checkedOutAt is null and b.deleted = false
        WHERE r.deleted = false
        GROUP BY r, r.type, r.region
    """)
    List<RoomWithBookingCountView> findAllWithBookingCount();

    Optional<Room> findByIdAndDeletedFalse(String roomId);

    <T> Optional<T> findByIdAndDeletedFalse(String roomId, Class<T> tClass);

    boolean existsByTypeIdAndDeletedFalse(Long roomTypeId);

    boolean existsByRegionIdAndDeletedIsFalse(String id);

    Long countByDeletedIsFalse();

    Long countByDeletedIsFalseAndStatusIs(RoomStatus roomStatus);

    @Query(
            value = """
                select count(*) from (
                    select p.MaPhong from Phong p
                    left join PhieuThue pt on pt.MaPhong = p.MaPhong and pt.TrangThai = 0 and pt.NgayTra is NULL
                    where p.TrangThai = 0
                    group by p.MaPhong
                    having count(pt.MaPhieuThue) = 0
                ) as emptyRooms
            """,
            nativeQuery = true
    )
    Long countEmptyRooms();

    boolean existsByIdAndDeletedIsFalse(String id);
}
