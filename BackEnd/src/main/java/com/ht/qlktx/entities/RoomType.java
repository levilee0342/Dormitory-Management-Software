package com.ht.qlktx.entities;

import com.ht.qlktx.enums.Sex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LoaiPhong")
@Check(constraints = "SoNguoi >= 0 AND DonGia >= 0")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLoaiPhong")
    private Long id;

    @Column(name = "TenLoaiPhong", columnDefinition = "NVARCHAR(100) NOT NULL UNIQUE")
    private String name;

    @Column(nullable = false, name = "SoNguoi")
    private int capacity;

    @Column(nullable = false, precision = 10, scale = 2, name = "DonGia")
    private BigDecimal price;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;
}
