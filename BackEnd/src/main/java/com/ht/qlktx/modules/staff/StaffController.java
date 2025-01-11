package com.ht.qlktx.modules.staff;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.staff.dtos.CreateStaffDto;
import com.ht.qlktx.modules.staff.dtos.UpdateStaffDto;
import com.ht.qlktx.modules.staff.dtos.UpdateProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/staffs")
public class StaffController {
    private final StaffService staffService;
    @GetMapping
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Iterable<Staff>>> findAll() {
        var staffs = staffService.findAll();
        return ResponseEntity.ok(
                Response.<Iterable<Staff>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách nhân viên thành công")
                        .data(staffs)
                        .build()
        );
    }

    @GetMapping("/non-account")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Iterable<Staff>>> findAllNonAccount() {
        var staffs = staffService.findAllNonAccount();
        return ResponseEntity.ok(
                Response.<Iterable<Staff>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách nhân viên chưa có tài khoản thành công")
                        .data(staffs)
                        .build()
        );
    }

    @GetMapping(path = "/lookup")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Iterable<Staff>>> lookUpByIdOrName(@RequestParam() String keyword) {
        var staffs = staffService.lookUpByIdOrName(keyword);
        return ResponseEntity.ok(
                Response.<Iterable<Staff>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Tìm kiếm nhân viên thành công")
                        .data(staffs)
                        .build()
        );
    }

    @GetMapping(path = "/{staffId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Staff>> findById(@PathVariable String staffId) {
        var staff = staffService.findById(staffId);
        return ResponseEntity.ok(
                Response.<Staff>builder()
                        .status(HttpStatus.OK.value())
                        .message("Tìm nhân viên thành công")
                        .data(staff)
                        .build()
        );
    }

    @PostMapping
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Staff>> create(@Valid @RequestBody CreateStaffDto createStaffDto) {
        var staff = staffService.create(createStaffDto);
        return ResponseEntity.created(null).body(
                Response.<Staff>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo nhân viên thành công")
                        .data(staff)
                        .build()
        );
    }

    @PutMapping(path = "/profile")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Staff>> update(@RequestAttribute("sub") String sub, @Valid @RequestBody UpdateProfileDto updateProfileDto) {
        var staff = staffService.update(sub, updateProfileDto);
        return ResponseEntity.ok(
                Response.<Staff>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật thông tin thành công")
                        .data(staff)
                        .build()
        );
    }

    @PutMapping(path = "/{staffId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Staff>> update(@PathVariable String staffId, @Valid @RequestBody UpdateStaffDto updateStaffDto) {
        var staff = staffService.update(staffId, updateStaffDto);
        return ResponseEntity.ok(
                Response.<Staff>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật nhân viên thành công")
                        .data(staff)
                        .build()
        );
    }

    @DeleteMapping(path = "/{staffId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable String staffId) {
        staffService.delete(staffId);
        return ResponseEntity.ok(
                Response.<Staff>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa nhân viên thành công")
                        .build()
        );
    }
}
