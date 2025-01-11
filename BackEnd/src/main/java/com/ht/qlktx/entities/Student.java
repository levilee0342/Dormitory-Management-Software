package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ht.qlktx.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SinhVien")
@Check(constraints = "NgaySinh <= GETDATE()")
public class Student {
    @Id
    @Column(name = "MaSinhVien", length = 50)
    private String id;

    @ManyToOne
    @JoinColumn(name = "MaTaiKhoan")
    private Account account;

    @JsonProperty("first_name")
    @Column(nullable = false, name = "Ten", columnDefinition = "NVARCHAR(50)")
    private String firstName;

    @JsonProperty("last_name")
    @Column(nullable = false, name = "Ho", columnDefinition = "NVARCHAR(50)")
    private String lastName;

    @Column(nullable = false, name = "Phai", length = 10)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @JsonProperty("date_of_birth")
    @Column(name = "NgaySinh", nullable = false)
    private Date dateOfBirth;

    @Column(name = "DiaChi", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String address;

    @JsonProperty("phone")
    @Column(name = "Sdt", length = 15, unique = true)
    private String phone;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;
}
