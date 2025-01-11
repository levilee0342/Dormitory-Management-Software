package com.ht.qlktx.modules.auth;

import com.ht.qlktx.config.Response;
import com.ht.qlktx.modules.auth.dtos.ChangePasswordDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/password")
public class PasswordController {
    private final AuthService authService;
    @PostMapping("/change")
    public ResponseEntity<Response<?>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                    @RequestAttribute("sub") String sub) {
        authService.changePassword(sub, changePasswordDto);
        return ResponseEntity.ok(
                Response.builder()
                        .status(HttpStatus.OK.value())
                        .message("Mật khẩu của bạn đã được cập nhật")
                        .data(null)
                        .build()
        );
    }
}
