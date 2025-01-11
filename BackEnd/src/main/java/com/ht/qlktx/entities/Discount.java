package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GiamGia")
@Check(constraints = "NgayKetThuc > NgayBatDau")
public class Discount {
    @Id
    @Column(name = "MaGiamGia", length = 20)
    private String id;

    @Column(nullable = false, name = "NoiDung", columnDefinition = "NVARCHAR(255)", unique = true)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2, name = "PhanTram")
    private BigDecimal percentage;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private Staff staff;

    @Column(nullable = false, name = "NgayBatDau")
    @JsonProperty("start_date")
    private Date startDate;

    @Column(nullable = false, name = "NgayKetThuc")
    @JsonProperty("end_date")
    private Date endDate;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @JsonIgnore
    public boolean isAvailable() {
        return !deleted && startDate.before(new Date()) && endDate.after(new Date());
    }
}
