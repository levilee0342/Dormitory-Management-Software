package com.ht.qlktx.modules.account;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Account;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.account.dtos.CreateAccountDto;
import com.ht.qlktx.modules.account.dtos.UpdateAccountDto;
import com.ht.qlktx.modules.staff.dtos.UpdateStaffDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<List<Account>>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách tài khoản",
                accounts
        ));
    }

    @PostMapping
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Account>> create(@Valid @RequestBody CreateAccountDto createAccountDto) {
        Account newAccount = accountService.create(createAccountDto);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.CREATED.value(),
                "Tạo tài khoản thành công",
                newAccount
        ));
    }

    @PutMapping(path = "/{accountId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Account>> update(@PathVariable String accountId, @Valid @RequestBody UpdateAccountDto updateAccountDto) {
        var account = accountService.update(accountId, updateAccountDto);
        return ResponseEntity.ok(
                Response.<Account>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật tài khoản thành công")
                        .data(account)
                        .build()
        );
    }

    @DeleteMapping(path = "/{accountId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable String accountId) {
        accountService.delete(accountId);
        return ResponseEntity.ok(
                Response.<Account>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa tài khoản thành công")
                        .build()
        );
    }

    @GetMapping("/lookup")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<List<Account>>> lookupByUsernameOrEmail(@RequestParam() String keyword) {
        List<Account> accounts = accountService.lookupByUsernameOrEmail(keyword);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Tìm kiếm tài khoản thành công",
                accounts
        ));
    }
}
