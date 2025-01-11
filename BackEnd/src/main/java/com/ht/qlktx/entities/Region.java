package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ht.qlktx.enums.Sex;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DayPhong")
public class Region {
    @Id
    @Column(name = "MaDay", length = 20)
    private String id;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @Column(name = "TenDay", columnDefinition = "NVARCHAR(100) NOT NULL UNIQUE")
    private String name;

    @Column(nullable = false, name = "GioiTinh", length = 10)
    @Enumerated(EnumType.STRING)
    private Sex sex;
}
