package com.ht.qlktx.modules.invoice;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Booking;
import com.ht.qlktx.entities.Invoice;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.invoice.dtos.CreateInvoiceDto;
import com.ht.qlktx.modules.invoice.dtos.UpdateInvoiceDto;
import com.ht.qlktx.modules.staff.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final BookingRepository bookingRepository;
    private final StaffRepository staffRepository;
    public Invoice create(CreateInvoiceDto createInvoiceDto, String sub) {
        var booking = bookingRepository.findByIdAndDeletedIsFalse(createInvoiceDto.getBookingId()).orElseThrow(
                () -> new HttpException("Không tìm thấy phiếu thuê", HttpStatus.BAD_REQUEST)
        );
        var staff = staffRepository.findByIdAndDeletedIsFalse(sub).orElseThrow(
                () -> new HttpException("Nhân viên không tồn tại", HttpStatus.BAD_REQUEST)
        );

        var total = createInvoiceDto.getTotal();

        if (booking.getDiscount() != null && booking.getDiscount().isAvailable()) {
            var percentage = booking.getDiscount().getPercentage();
            total = total.multiply(BigDecimal.valueOf(1)
                    .subtract(percentage.divide(BigDecimal.valueOf(100))));
        }

        return create(booking, staff, total);
    }

    public Invoice create(Booking booking, Staff staff, BigDecimal total) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 1);

        Invoice invoice = Invoice.builder()
                .booking(booking)
                .staff(staff)
                .total(total)
                .createdAt(new Date())
                .dueDate(calendar.getTime())
                .build();
        return invoiceRepository.save(invoice);
    }

    public Invoice findById(Long id) {
        return invoiceRepository.findByIdAndDeletedIsFalse(id).orElseThrow(
                () -> new HttpException("Không tìm thấy hóa đơn", HttpStatus.BAD_REQUEST)
        );
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAllByDeletedIsFalse();
    }

    public List<Invoice> findAllUnpaid() {
        return invoiceRepository.findAllByDeletedIsFalseAndPaidAtIsNull();
    }

    public List<Invoice> findAllPaid() {
        return invoiceRepository.findAllByDeletedIsFalseAndPaidAtIsNotNull();
    }

    public Invoice update(Long id, UpdateInvoiceDto updateInvoiceDto) {
        var invoice = findById(id);
        if (invoice.isPaid()) {
            throw new HttpException("Không thể cập nhật hóa đơn đã thanh toán", HttpStatus.BAD_REQUEST);
        }

        Optional.ofNullable(updateInvoiceDto.getPaidAt()).ifPresent(paidAt -> {
            if (invoice.getCreatedAt().after(paidAt))
                throw new HttpException("Ngày thanh toán không hợp lệ", HttpStatus.BAD_REQUEST);
            invoice.setPaidAt(paidAt);
        });

        Optional.ofNullable(updateInvoiceDto.getTotal()).ifPresent(invoice::setTotal);

        return invoiceRepository.save(invoice);
    }

    public List<Invoice> findAllByStudentId(String studentId) {
        return invoiceRepository.findAllByBookingStudentIdAndDeletedIsFalse(studentId);
    }

    public List<Invoice> findAllByBookingId(Long bookingId) {
        return invoiceRepository.findAllByBookingIdAndDeletedIsFalse(bookingId);
    }

    public void delete(Long id) {
        var invoice = findById(id);
        if (invoice.isPaid()) {
            throw new HttpException("Không thể xóa hóa đơn đã thanh toán", HttpStatus.BAD_REQUEST);
        }
        invoice.setDeleted(true);
        invoiceRepository.save(invoice);
    }

    public void delete(List<Invoice> invoices) {
        invoices.forEach(invoice -> {
            invoice.setDeleted(true);
        });
        invoiceRepository.saveAll(invoices);
    }

    public List<Invoice> findPaidInvoicesByStudentId(String studentId) {
        return invoiceRepository.findAllByDeletedIsFalseAndPaidAtIsNotNullAndBookingStudentId(studentId);
    }

    public List<Invoice> findUnpaidInvoicesByStudentId(String studentId) {
        return invoiceRepository.findAllByDeletedIsFalseAndPaidAtIsNullAndBookingStudentId(studentId);
    }
}
