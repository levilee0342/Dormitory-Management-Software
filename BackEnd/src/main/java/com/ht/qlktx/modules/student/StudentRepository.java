package com.ht.qlktx.modules.student;

import com.ht.qlktx.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findAllByDeletedIsFalse();

    @Query("SELECT u FROM Student u WHERE u.deleted = false and (u.id LIKE %:keyword% OR concat(u.firstName, ' ', u.lastName) LIKE %:keyword%)")
    List<Student> lookupByIdOrName(String keyword);

    Optional<Student> findByIdAndDeletedIsFalse(String studentId);

    Long countByDeletedIsFalse();

    @Query(
            value = """
                select count(*) from (
                    select sv.MaSinhVien from SinhVien sv
                    left join PhieuThue pt on pt.MaSinhVien = sv.MaSinhVien and pt.TrangThai = 0
                    where sv.TrangThai = 0
                    group by sv.MaSinhVien
                    HAVING count(pt.MaPhieuThue) > 0
                ) as bookedStudents
            """,
            nativeQuery = true
    )
    Long countBookedStudents();

    List<Student> findAllByAccountIsNullAndDeletedIsFalse();

    @Query("SELECT u FROM Student u WHERE u.account.username = :accountId AND u.deleted = false")
    Optional<Student> findByAccountAndDeletedIsFalse(String accountId);

    boolean existsByAccountUsernameAndDeletedIsFalse(String accountId);

    boolean existsByAccountUsername(String accountId);

    boolean existsByIdAndDeletedIsFalse(String id);
}
