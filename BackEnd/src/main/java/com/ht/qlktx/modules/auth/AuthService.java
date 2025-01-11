package com.ht.qlktx.modules.auth;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Account;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.entities.Student;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.filter.JwtService;
import com.ht.qlktx.modules.account.AccountRepository;
import com.ht.qlktx.modules.auth.dtos.AuthenticateDto;
import com.ht.qlktx.modules.auth.dtos.ChangePasswordDto;
import com.ht.qlktx.modules.staff.StaffRepository;
import com.ht.qlktx.modules.student.StudentRepository;
import com.ht.qlktx.utils.MailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AuthService.class);
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    public AuthenticationResponse<?> authenticate(AuthenticateDto authenticateDto) {
        String errorMessage = "Tài khoản hoặc mật khẩu không hợp lệ";

        Account account = accountRepository.findByUsernameOrEmail(authenticateDto.getUsername(), authenticateDto.getUsername())
                .orElseThrow(() -> new HttpException(errorMessage, HttpStatus.UNAUTHORIZED));

        if (passwordEncoder.matches(authenticateDto.getPassword(), account.getPassword())) {
            String accessToken = jwtService.generateAccessToken(account);

            Object user;

            if (account.getRole().getRole().equals(Role.STUDENT.toString())) {
                user = studentRepository.findByIdAndDeletedIsFalse(account.getUsername())
                        .orElseThrow(() -> new HttpException(errorMessage, HttpStatus.UNAUTHORIZED));
            } else {
                user = staffRepository.findByIdAndDeletedIsFalse(account.getUsername())
                        .orElseThrow(() -> new HttpException(errorMessage, HttpStatus.UNAUTHORIZED));
            }

            return new AuthenticationResponse<>(
                    new Credentials(accessToken),
                    user
            );
        }

        throw new HttpException(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    public void forgotPassword(String email) {
        var account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("Tài khoản không tồn tại", HttpStatus.NOT_FOUND));

        var token = jwtService.generateResetPasswordToken(email);
        account.setResetPasswordToken(token);
        accountRepository.save(account);
        CompletableFuture.runAsync(() -> {
            try {
                mailService.sendForgotPasswordMail(email, token);
            } catch (Exception e) {
                logger.error("Có lỗi xảy ra khi gửi mail: {}", e.getMessage());
            }
        });
    }

    public void resetPassword(String token, String newPassword) {
        var result = jwtService.verifyResetPasswordToken(token);

        if (!result.isTokenValid())
            throw new HttpException("Token không hợp lệ", HttpStatus.FORBIDDEN);

        String email = result.subject();

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new HttpException("Tài khoản không tồn tại", HttpStatus.NOT_FOUND));

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setResetPasswordToken(null);
        accountRepository.save(account);
    }

    public void changePassword(String accountId, ChangePasswordDto changePasswordDto) {
        String errorMessage = "Tài khoản hoặc mật khẩu không hợp lệ";
        var account = accountRepository.findById(accountId).orElseThrow(() -> new HttpException(errorMessage, HttpStatus.BAD_REQUEST));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), account.getPassword()))
            throw new HttpException(errorMessage, HttpStatus.BAD_REQUEST);

        account.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        accountRepository.save(account);
    }
}
