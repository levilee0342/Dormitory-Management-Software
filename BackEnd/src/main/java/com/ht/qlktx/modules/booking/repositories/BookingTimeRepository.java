package com.ht.qlktx.modules.booking.repositories;

import com.ht.qlktx.entities.BookingTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingTimeRepository extends JpaRepository<BookingTime, Long> {
    List<BookingTime> findAllByOpenIsTrueAndStartDateIsAfter(Date date);

    Optional<BookingTime> findByIdAndOpenIsTrueAndStartDateIsAfter(Long bookingTimeId, Date date);

    Optional<BookingTime> findByIdAndDeletedIsFalse(Long bookingTimeId);

    List<BookingTime> findAllByOpenIsTrueAndStartDateIsAfterAndDeletedIsFalse(Date date);

    Optional<BookingTime> findByIdAndOpenIsTrueAndStartDateIsAfterAndDeletedIsFalse(Long bookingTimeId, Date date);

    List<BookingTime> findAllByDeletedIsFalse();

    List<BookingTime> findAllByOpenIsTrueAndEndDateIsAfterAndDeletedIsFalse(Date date);

    Optional<BookingTime> findByIdAndOpenIsTrueAndEndDateIsAfterAndDeletedIsFalse(Long bookingTimeId, Date date);

    boolean existsByStaffId(String staffId);
}
