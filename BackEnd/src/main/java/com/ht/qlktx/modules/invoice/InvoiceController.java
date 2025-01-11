package com.ht.qlktx.modules.invoice;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Invoice;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.invoice.dtos.CreateInvoiceDto;
import com.ht.qlktx.modules.invoice.dtos.UpdateInvoiceDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;
    @GetMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Invoice>>> findAll() {
        var invoices = invoiceService.findAll();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn",
                invoices
        ));
    }

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Invoice>> create(@Valid @RequestBody CreateInvoiceDto createInvoiceDto,
                                                    @RequestAttribute("sub") String sub) {
        var invoice = invoiceService.create(createInvoiceDto, sub);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.CREATED.value(),
                "Tạo hóa đơn thành công",
                invoice
        ));
    }

    @GetMapping("/unpaid")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Invoice>>> findAllUnpaid() {
        var invoices = invoiceService.findAllUnpaid();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn chưa thanh toán",
                invoices
        ));
    }

    @GetMapping("/paid")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Invoice>>> findAllPaid() {
        var invoices = invoiceService.findAllPaid();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn đã thanh toán",
                invoices
        ));
    }

    @PutMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Invoice>> update(@PathVariable Long id, @RequestBody UpdateInvoiceDto updateInvoiceDto) {
        var invoice = invoiceService.update(id, updateInvoiceDto);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Cập nhật hóa đơn thành công",
                invoice
        ));
    }

    @DeleteMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<String>> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Xóa hóa đơn thành công",
                null
        ));
    }

    @GetMapping("/student/{studentId}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Invoice>>> findAllByStudentId(@PathVariable String studentId) {
        var invoices = invoiceService.findAllByStudentId(studentId);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn",
                invoices
        ));
    }

    @GetMapping("/booking/{bookingId}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Invoice>>> findByBookingId(@PathVariable Long bookingId) {
        var invoices = invoiceService.findAllByBookingId(bookingId);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn",
                invoices
        ));
    }

    @GetMapping("paid/student/{studentId}")
    @RequiredRole({Role.STUDENT})
    public ResponseEntity<Response<Iterable<Invoice>>> findPaidInvoicesByStudentId(@PathVariable String studentId) {
        var invoices = invoiceService.findPaidInvoicesByStudentId(studentId);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn của sinh viên",
                invoices
        ));
    }

    @GetMapping("unpaid/student/{studentId}")
    @RequiredRole({Role.STUDENT})
    public ResponseEntity<Response<Iterable<Invoice>>> findUnpaidInvoicesByStudentId(@PathVariable String studentId) {
        var invoices = invoiceService.findUnpaidInvoicesByStudentId(studentId);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách hóa đơn của sinh viên",
                invoices
        ));
    }
}
