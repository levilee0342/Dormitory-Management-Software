package com.ht.qlktx.modules.discount;

import com.ht.qlktx.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, String>{
    List<Discount> findAllByDeletedIsFalse();

    boolean existsByDescription(String description);

    List<Discount> findAllByDeletedIsFalseAndStartDateBeforeAndEndDateAfter(Date date, Date date1);

    Optional<Discount> findByIdAndDeletedIsFalse(String id);

    Optional<Discount> findByIdAndDeletedIsFalseAndStartDateBeforeAndEndDateAfter(String id, Date date, Date date1);

    boolean existsByIdAndDeletedIsFalse(String id);

    boolean existsByDescriptionAndDeletedIsFalse(String description);

    boolean existsByStaffId(String staffId);
}
