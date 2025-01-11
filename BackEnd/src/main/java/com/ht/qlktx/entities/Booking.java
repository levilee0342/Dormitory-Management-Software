package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuThue")
@Check(constraints = "NgayTra >= NgayLap")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhieuThue")
    private Long id;

    @CreationTimestamp
    @Column(name = "NgayLap", nullable = false)
    @JsonProperty("created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "MaThoiGianThue", nullable = false)
    @JsonProperty("booking_time")
    private BookingTime bookingTime;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienNhan", nullable = false)
    @JsonProperty("checkin_staff")
    private Staff checkinStaff;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "MaPhong", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "MaSinhVien", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "MaNhanVienTra", nullable = true)
    @JsonProperty("checkout_staff")
    private Staff checkoutStaff;

    @Column(name = "NgayTra", nullable = true)
    @JsonProperty("checked_out_at")
    private Date checkedOutAt;

    @ManyToOne
    @JoinColumn(name = "MaGiamGia", nullable = true)
    private Discount discount;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @JsonIgnore
    public BigDecimal getTotalPrice() {
        var unitPrice = room.getType().getPrice();
        var duration = bookingTime.getDurationInMonths();
        if (discount != null) {
            var percentage = discount.getPercentage();
            return unitPrice.multiply(BigDecimal.valueOf(duration))
                    .multiply(BigDecimal.valueOf(1)
                            .subtract(percentage.divide(BigDecimal.valueOf(100))));
        }
        return unitPrice.multiply(BigDecimal.valueOf(duration));
    }

    @JsonIgnore
    public boolean isCheckedOut() {
        return checkedOutAt != null;
    }
}
