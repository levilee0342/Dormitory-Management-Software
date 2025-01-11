package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ht.qlktx.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phong")
public class Room {
    @Id
    @Column(name = "MaPhong", length = 20)
    private String id;

    @ManyToOne
    @JoinColumn(name = "MaDay", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "MaLoaiPhong", nullable = false)
    private RoomType type;

    @Column(nullable = false, name = "TrangThai", columnDefinition = "BIT DEFAULT 0")
    private boolean deleted;

    @Column(nullable = false, name = "TrangThaiPhong", length = 20)
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @JsonManagedReference
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, targetEntity = Booking.class)
    private List<Booking> bookings;
}
