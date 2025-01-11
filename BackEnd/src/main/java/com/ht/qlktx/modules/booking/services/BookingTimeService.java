package com.ht.qlktx.modules.booking.services;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.BookingTime;
import com.ht.qlktx.modules.booking.dtos.CreateBookingTimeDto;
import com.ht.qlktx.modules.booking.dtos.UpdateBookingTimeDto;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.booking.repositories.BookingTimeRepository;
import com.ht.qlktx.modules.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingTimeService {
    private final StaffService staffService;
    private final BookingTimeRepository bookingTimeRepository;
    private final BookingRepository bookingRepository;

    public BookingTime create(CreateBookingTimeDto createBookingTimeDto, String staffId) {
        if (createBookingTimeDto.getEndDate().before(createBookingTimeDto.getStartDate()) || createBookingTimeDto.getEndDate().equals(createBookingTimeDto.getStartDate()))
            throw new HttpException("Thời gian bắt đầu phải bé hơn thời gian kết thúc", HttpStatus.BAD_REQUEST);

        BookingTime bookingTime = BookingTime.builder()
                .startDate(createBookingTimeDto.getStartDate())
                .endDate(createBookingTimeDto.getEndDate())
                .description(createBookingTimeDto.getDescription())
                .open(createBookingTimeDto.isOpen())
                .staff(staffService.findById(staffId))
                .build();

        return bookingTimeRepository.save(bookingTime);
    }

    public void delete(Long id) {
        var bookingTime = findById(id);
        if (bookingRepository.existsByBookingTimeId(id)) {
            throw new HttpException("Không thể xóa vì đã có phiếu thuê đang sử dụng", HttpStatus.BAD_REQUEST);
        }

        bookingTime.setDeleted(true);
        bookingTimeRepository.save(bookingTime);
    }

    public BookingTime update(Long id, UpdateBookingTimeDto updateBookingTimeDto) {
        var bookingTime = findById(id);

        Optional.ofNullable(updateBookingTimeDto.getDescription()).ifPresent(bookingTime::setDescription);
        Optional.ofNullable(updateBookingTimeDto.getEndDate()).ifPresent(bookingTime::setEndDate);
        Optional.ofNullable(updateBookingTimeDto.getStartDate()).ifPresent(bookingTime::setStartDate);
        Optional.of(updateBookingTimeDto.isOpen()).ifPresent(bookingTime::setOpen);

        if (bookingTime.getEndDate().before(bookingTime.getStartDate()) || bookingTime.getEndDate().equals(bookingTime.getStartDate()))
            throw new HttpException("Thời gian bắt đầu phải bé hơn thời gian kết thúc", HttpStatus.BAD_REQUEST);

        return bookingTimeRepository.save(bookingTime);
    }

    public List<BookingTime> findAll() {
        return bookingTimeRepository.findAllByDeletedIsFalse();
    }

    public List<BookingTime> findAllAvailable() {
        return bookingTimeRepository.findAllByOpenIsTrueAndEndDateIsAfterAndDeletedIsFalse(new Date());
    }

    public BookingTime findById(Long bookingTimeId) {
        return bookingTimeRepository.findByIdAndDeletedIsFalse(bookingTimeId).orElseThrow(() -> new HttpException("Không tìm thấy thời gian thuê", HttpStatus.BAD_REQUEST));
    }

    public BookingTime findByAvailableId(Long bookingTimeId) {
        return bookingTimeRepository.findByIdAndOpenIsTrueAndEndDateIsAfterAndDeletedIsFalse(bookingTimeId, new Date()).orElseThrow(() -> new HttpException("Không tìm thấy thời gian thuê", HttpStatus.BAD_REQUEST));
    }
}
