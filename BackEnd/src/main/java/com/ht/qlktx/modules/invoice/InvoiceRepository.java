package com.ht.qlktx.modules.invoice;

import com.ht.qlktx.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByDeletedIsFalse();

    List<Invoice> findAllByDeletedIsFalseAndPaidAtIsNull();

    List<Invoice> findAllByDeletedIsFalseAndPaidAtIsNotNull();

    Optional<Invoice> findByIdAndDeletedIsFalse(Long id);

    List<Invoice> findAllByBookingStudentIdAndDeletedIsFalse(String studentId);

    List<Invoice> findAllByBookingIdAndDeletedIsFalse(Long bookingId);

    Long countByDeletedIsFalse();

    Long countByDeletedIsFalseAndPaidAtIsNull();

    List<Invoice> findAllByDeletedIsFalseAndPaidAtIsNotNullAndBookingStudentId(String studentId);

    List<Invoice> findAllByDeletedIsFalseAndPaidAtIsNullAndBookingStudentId(String studentId);
}
