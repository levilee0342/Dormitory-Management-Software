package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HoaDon")
@Check(constraints = "TongTienPhong >= 0")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHoaDon")
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2, name = "TongTienPhong")
    private BigDecimal total;

    @Column(nullable = false, name = "HanThanhToan")
    @JsonProperty("due_date")
    private Date dueDate;

    @Column(nullable = true, name = "NgayThanhToan")
    @JsonProperty("paid_at")
    private Date paidAt;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "MaPhieuThue")
    private Booking booking;

    @CreationTimestamp
    @Column(name = "NgayLap")
    @JsonProperty("created_at")
    private Date createdAt;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @JsonIgnore
    public boolean isPaid() {
        return paidAt != null;
    }
}
