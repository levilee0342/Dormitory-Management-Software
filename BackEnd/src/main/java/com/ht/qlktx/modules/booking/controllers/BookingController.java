package com.ht.qlktx.modules.booking.controllers;


import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Booking;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.booking.dtos.CheckoutDto;
import com.ht.qlktx.projections.BookingView;
import com.ht.qlktx.modules.booking.dtos.CreateBookingDto;
import com.ht.qlktx.modules.booking.services.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(path = "/api/v1/booking")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingView>>> findAll() {
        var bookings = bookingService.findAll();
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("current")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingView>>> findAllCurrent() {
        var bookings = bookingService.findAllCurrent();
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<BookingView>> findById(@PathVariable Long id) {
        var booking = bookingService.findById(id, BookingView.class);
        return ResponseEntity.ok(
                Response.<BookingView>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy phiếu thuê thành công")
                        .data(booking)
                        .build()
        );
    }

    @GetMapping("/student/{studentId}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingView>>> findAllByStudentId(@PathVariable String studentId) {
        var bookings = bookingService.findAllByStudentId(studentId);
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<Response<List<BookingView>>> findAllByRoomId(@PathVariable String roomId) {
        var bookings = bookingService.findAllByRoomId(roomId);
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("/room/{roomId}/checked-out")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingView>>> findAllCheckedOutByRoomId(@PathVariable String roomId) {
        var bookings = bookingService.findAllCheckedOutByRoomId(roomId);
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê đã trả phòng thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("checked-out")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<List<BookingView>>> findAllCheckedOut() {
        var bookings = bookingService.findAllCheckedOut();
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê đã trả phòng thành công")
                        .data(bookings)
                        .build()
        );
    }

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Booking>> create(@Valid @RequestBody CreateBookingDto createBookingDto,
                                                    @RequestAttribute(name = "sub") String sub) {
        var booking = bookingService.create(createBookingDto, sub);
        return ResponseEntity.created(null).body(
                Response.<Booking>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo phiếu thuê thành công")
                        .data(booking)
                        .build()
        );
    }

    @GetMapping("/price")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<BigDecimal>> calculatePrice(@RequestParam(name = "booking_time_id") Long bookingTimeId,
                                                               @RequestParam(name = "room_id") String roomId,
                                                               @RequestParam(name = "discount_id", required = false) String discountId) {
        var price = bookingService.calculatePrice(bookingTimeId, roomId, discountId);
        return ResponseEntity.ok(
                Response.<BigDecimal>builder()
                        .status(HttpStatus.OK.value())
                        .message("Tính giá thuê phòng thành công")
                        .data(price)
                        .build()
        );
    }

    @PostMapping("/check-out/{bookingId}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Booking>> checkOut(@Valid @RequestBody CheckoutDto checkoutDto,
                                                      @PathVariable Long bookingId,
                                                      @RequestAttribute(name = "sub") String sub) {
        var booking = bookingService.checkOut(bookingId, sub, checkoutDto.getCheckoutAt());
        return ResponseEntity.ok(
                Response.<Booking>builder()
                        .status(HttpStatus.OK.value())
                        .message("Trả phòng thành công")
                        .data(booking)
                        .build()
        );
    }

    @DeleteMapping("{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok(
                Response.<Booking>builder()
                        .status(HttpStatus.OK.value())
                        .message("Phiếu thuê được huỷ thành công")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/my-booking")
    @RequiredRole({Role.STUDENT})
    public ResponseEntity<Response<List<BookingView>>> MyBooking(@RequestAttribute(name = "sub") String studentId){
        var bookings = bookingService.findAllCurrentByStudentId(studentId);
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách phiếu thuê hiện tại thành công")
                        .data(bookings)
                        .build()
        );
    }

    @GetMapping("/my-lastbooking")
    @RequiredRole({Role.STUDENT})
    public ResponseEntity<Response<List<BookingView>>> MyLastBooking(@RequestAttribute(name = "sub") String studentId){
        var bookings = bookingService.findAllLastBookingByStudentId(studentId);
        return ResponseEntity.ok(
                Response.<List<BookingView>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách các phiếu thuê cũ thành công")
                        .data(bookings)
                        .build()
        );
    }
}
