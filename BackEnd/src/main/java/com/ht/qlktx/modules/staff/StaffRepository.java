package com.ht.qlktx.modules.staff;

import com.ht.qlktx.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, String> {
    List<Staff> findAllByDeletedIsFalse();

    Optional<Staff> findByIdAndDeletedIsFalse(String staffId);

    @Query("SELECT u FROM Staff u WHERE u.deleted = false and (u.id LIKE %:keyword% OR concat(u.firstName, ' ', u.lastName) LIKE %:keyword%)")
    List<Staff> lookUpByIdOrName(String keyword);

    List<Staff> findAllByAccountIsNullAndDeletedIsFalse();

    @Query("SELECT u FROM Staff u WHERE u.account.username = :accountId AND u.deleted = false")
    Optional<Staff> findByAccountAndDeletedIsFalse(String accountId);

    boolean existsByAccountUsernameAndDeletedIsFalse(String accountId);

    boolean existsByAccountUsername(String accountId);

    boolean existsByIdAndDeletedIsFalse(String id);
}
