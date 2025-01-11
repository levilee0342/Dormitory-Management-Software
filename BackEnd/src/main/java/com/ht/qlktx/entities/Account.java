package com.ht.qlktx.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan")
public class Account {
    @Id
    @Column(name = "MaTaiKhoan", length = 50)
    private String username;

    @Column(name = "MatKhau", nullable = false)
    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "MaVaiTro", nullable = false)
    private Role role;

    @Column(nullable = false, unique = true, name = "Email")
    private String email;

    @Column(name = "MaDatLaiMatKhau")
    @JsonIgnore
    @JsonProperty("reset_password_token")
    private String resetPasswordToken;
}
