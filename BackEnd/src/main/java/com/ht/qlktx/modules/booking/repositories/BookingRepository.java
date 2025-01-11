package com.ht.qlktx.modules.booking.repositories;

import com.ht.qlktx.entities.Booking;
import com.ht.qlktx.projections.BookingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByBookingTimeId(Long id);

    boolean existsByRoomRegionIdAndDeletedIsFalse(String id);

    List<BookingView> findAllByDeletedIsFalse();

    List<BookingView> findAllByDeletedIsFalseAndCheckedOutAtIsNotNull();

    @Query("SELECT b FROM Booking b WHERE b.deleted = false AND b.room.id = :id and b.checkedOutAt is null")
    List<BookingView> findAllByRoomIdAndDeletedIsFalse(String id);

    Long countByRoomIdAndDeletedIsFalse(String id);

    Optional<Booking> findByIdAndDeletedIsFalse(Long id);

    <T> Optional<T> findByIdAndDeletedIsFalse(Long id, Class<T> type);

    List<BookingView> findAllByRoomIdAndDeletedIsFalseAndCheckedOutAtIsNotNull(String roomId);

    Long countByRoomIdAndDeletedIsFalseAndCheckedOutAtIsNull(String id);

    boolean existsByDiscountId(String id);

    List<BookingView> findAllByStudentIdAndDeletedIsFalse(String studentId);

    boolean existsByRoomIdAndDeletedIsFalseAndCheckedOutAtIsNull(String roomId);

    boolean existsByRoomIdAndDeletedIsFalse(String roomId);

    List<BookingView> findAllByDeletedIsFalseAndCheckedOutAtIsNull();

    @Query("SELECT b FROM Booking b WHERE b.student.id = :studentId AND b.deleted = false AND b.checkedOutAt is null")
    List<BookingView> findAllByStudentIdAndDeletedIsFalseAndCheckedOutAtIsNull(String studentId);

    @Query("SELECT b FROM Booking b WHERE b.student.id = :studentId AND b.deleted = false AND b.checkedOutAt is not null")
    List<BookingView> findAllByStudentIdAndDeletedIsFalseAndCheckedOutAtIsNotNull(String studentId);

    boolean existsByStudentId(String studentId);

    boolean existsByCheckinStaffIdOrCheckoutStaffId(String checkinStaffId, String checkoutStaffId);
}
