package com.ht.qlktx.modules.account;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Account;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.account.dtos.CreateAccountDto;
import com.ht.qlktx.modules.account.dtos.UpdateAccountDto;
import com.ht.qlktx.modules.staff.StaffService;
import com.ht.qlktx.modules.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final StudentService studentService;
    private final StaffService staffService;
    private final PasswordEncoder passwordEncoder;


    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public boolean isEmailExists(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> getAccountByUsernameOrEmail(String username, String email) {
        return accountRepository.findByUsernameOrEmail(username, email);
    }

    public Account findById(String accountId) {
        return accountRepository.findByUsername(accountId).orElseThrow(
                () -> new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST)
        );
    }

    public Account create(CreateAccountDto createAccountDto) {
        if (accountRepository.existsByUsername(createAccountDto.getUsername()) || accountRepository.existsByEmail(createAccountDto.getEmail())) {
            throw new HttpException("Tên đăng nhập hoặc email đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var role = roleRepository.findByRole(createAccountDto.getRole()).orElseThrow(
                () -> new HttpException("Không tồn tại quyền này", HttpStatus.BAD_REQUEST)
        );

        var account = Account.builder()
                .username(createAccountDto.getUsername())
                .password(passwordEncoder.encode(createAccountDto.getPassword()))
                .email(createAccountDto.getEmail())
                .role(role)
                .build();

        Account savedAccount = null;

        if (role.getRole().equals(Role.ADMIN.toString()) || role.getRole().equals(Role.STAFF.toString())) {
            var staff = staffService.findById(createAccountDto.getUserId());
            if (staff.getAccount() != null) {
                throw new HttpException("Nhân viên đã có tài khoản", HttpStatus.BAD_REQUEST);
            }
            savedAccount = accountRepository.save(account);
            staff.setAccount(account);
            staffService.save(staff);
        } else if (role.getRole().equals(Role.STUDENT.toString())) {
            var student = studentService.findById(createAccountDto.getUserId());
            if (student.getAccount() != null) {
                throw new HttpException("Sinh viên đã có tài khoản", HttpStatus.BAD_REQUEST);
            }
            savedAccount = accountRepository.save(account);
            student.setAccount(account);
            studentService.save(student);
        } else {
            throw new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST);
        }

        return savedAccount;
    }

    public List<Account> lookupByUsernameOrEmail(String keyword) {
        return accountRepository.lookupByUsernameOrEmail(keyword);
    }

    public Account update(String accountId, UpdateAccountDto updateAccountDto) {
        var account = findById(accountId);

        if (accountRepository.existsByEmailAndUsernameIsNot(updateAccountDto.getEmail(), account.getUsername())) {
            throw new HttpException("Email đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Optional.ofNullable(updateAccountDto.getEmail()).ifPresent(account::setEmail);
        Optional.ofNullable(updateAccountDto.getPassword()).ifPresent(password -> {
            account.setPassword(passwordEncoder.encode(password));
        });

        return accountRepository.save(account);
    }

    public void delete(String accountId) {
        var isAccountLinked = staffService.existsByAccountId(accountId) || studentService.existsByAccountId(accountId);

        if (isAccountLinked) {
            throw new HttpException("Không thể xóa tài khoản vì đã được cập nhật thông tin", HttpStatus.BAD_REQUEST);
        }

        accountRepository.deleteById(accountId);
    }
}
