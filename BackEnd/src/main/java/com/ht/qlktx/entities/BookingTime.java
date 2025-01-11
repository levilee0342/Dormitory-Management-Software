package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ThoiGianThue")
@Check(constraints = "NgayBatDau < NgayKetThuc")
public class BookingTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThoiGianThue")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private Staff staff;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(255)", nullable = false)
    private String description;

    @Column(name = "NgayBatDau", nullable = false)
    @JsonProperty("start_date")
    private Date startDate;

    @Column(name = "NgayKetThuc", nullable = false)
    @JsonProperty("end_date")
    private Date endDate;

    @Column(nullable = false, name = "TrangThaiMo")
    private boolean open;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @JsonIgnore
    public double getDurationInMonths() {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        var lastDayOfStartMonth = start.with(TemporalAdjusters.lastDayOfMonth());
        var firstDayOfEndMonth = end.with(TemporalAdjusters.firstDayOfMonth());
        var startMonthLength = (double)start.lengthOfMonth();
        var endMonthLength = (double)end.lengthOfMonth();
        if (lastDayOfStartMonth.isAfter(firstDayOfEndMonth)) { // same month
            return ChronoUnit.DAYS.between(start, end) / startMonthLength;
        }
        long months = ChronoUnit.MONTHS.between(lastDayOfStartMonth, firstDayOfEndMonth);
        double startFraction = ChronoUnit.DAYS.between(start, lastDayOfStartMonth.plusDays(1)) / startMonthLength;
        double endFraction = ChronoUnit.DAYS.between(firstDayOfEndMonth, end) / endMonthLength;
        return months + startFraction + endFraction;
    }
}
