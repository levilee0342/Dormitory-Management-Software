package com.ht.qlktx.modules.booking.controllers;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.BookingTime;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.booking.services.BookingTimeService;
import com.ht.qlktx.modules.booking.dtos.CreateBookingTimeDto;
import com.ht.qlktx.modules.booking.dtos.UpdateBookingTimeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(path = "/api/v1/booking/booking-time")
public class BookingTimeController {
    private final BookingTimeService bookingTimeService;

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<BookingTime>> create(@Valid @RequestBody CreateBookingTimeDto createBookingTimeDto,
                                                        @RequestAttribute(name = "sub") String sub) {
        var bookingTime = bookingTimeService.create(createBookingTimeDto, sub);
        return ResponseEntity.created(null).body(
                Response.<BookingTime>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo thời gian thuê thành công")
                        .data(bookingTime)
                        .build()
        );
    }

    @GetMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingTime>>> findAll() {
        var bookingTimes = bookingTimeService.findAll();
        return ResponseEntity.ok(
                Response.<List<BookingTime>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách thời gian thuê thành công")
                        .data(bookingTimes)
                        .build()
        );
    }

    @GetMapping("available")
    public ResponseEntity<Response<List<BookingTime>>> findAllAvailable() {
        var bookingTimes = bookingTimeService.findAllAvailable();
        return ResponseEntity.ok(
                Response.<List<BookingTime>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách thời gian thuê thành công")
                        .data(bookingTimes)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<BookingTime>> update(@Valid @RequestBody UpdateBookingTimeDto updateBookingTimeDto,
                                                        @PathVariable Long id) {
        var bookingTime = bookingTimeService.update(id, updateBookingTimeDto);
        return ResponseEntity.ok(
                Response.<BookingTime>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật thời gian thuê thành công")
                        .data(bookingTime)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable Long id) {
        bookingTimeService.delete(id);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa thời gian thuê thành công")
                        .data(null)
                        .build()
        );
    }
}
