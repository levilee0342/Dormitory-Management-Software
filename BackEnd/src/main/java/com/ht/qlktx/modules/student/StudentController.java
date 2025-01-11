package com.ht.qlktx.modules.student;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Student;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.student.dtos.CreateStudentDto;
import com.ht.qlktx.modules.student.dtos.UpdateProfileDto;
import com.ht.qlktx.modules.student.dtos.UpdateStudentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @RequiredRole({Role.ADMIN, Role.STAFF})
    public ResponseEntity<Response<Iterable<Student>>> findAllStudents() {
        var users = studentService.findAll();
        return ResponseEntity.ok(
                Response.<Iterable<Student>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách người dùng thành công")
                        .data(users)
                        .build()
        );
    }

    @GetMapping("/non-account")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Iterable<Student>>> findAllNonAccount() {
        var users = studentService.findAllNonAccount();
        return ResponseEntity.ok(
                Response.<Iterable<Student>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy danh sách người dùng chưa có tài khoản thành công")
                        .data(users)
                        .build()
        );
    }

    @GetMapping(path = "/lookup")
    @RequiredRole({Role.ADMIN, Role.STAFF})
    public ResponseEntity<Response<Iterable<Student>>> lookupStudentsByIdOrName(@RequestParam String keyword) {
        var users = studentService.lookupByIdOrName(keyword);
        return ResponseEntity.ok(
                Response.<Iterable<Student>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Tìm kiếm người dùng thành công")
                        .data(users)
                        .build()
        );
    }

    @GetMapping(path = "/{studentId}")
    @RequiredRole({Role.ADMIN, Role.STAFF})
    public ResponseEntity<Response<Student>> findById(@PathVariable String studentId) {
        var user = studentService.findById(studentId);
        return ResponseEntity.ok(
                Response.<Student>builder()
                        .status(HttpStatus.OK.value())
                        .message("Tìm người dùng thành công")
                        .data(user)
                        .build()
        );
    }

    @PostMapping
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Student>> create(@Valid @RequestBody CreateStudentDto createStudentDto) {
        var user = studentService.create(createStudentDto);
        return ResponseEntity.created(null).body(
                Response.<Student>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo người dùng thành công")
                        .data(user)
                        .build()
        );
    }

    @PutMapping(path = "/profile")
    @RequiredRole({Role.STUDENT})
    public ResponseEntity<Response<Student>> update(@RequestAttribute("sub") String sub, @Valid @RequestBody UpdateProfileDto updateProfileDto) {
        var user = studentService.update(sub, updateProfileDto);
        return ResponseEntity.ok(
                Response.<Student>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật thông tin thành công")
                        .data(user)
                        .build()
        );
    }

    @PutMapping(path = "/{studentId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<Student>> update(@PathVariable String studentId, @Valid @RequestBody UpdateStudentDto updateStudentDto) {
        var user = studentService.update(studentId, updateStudentDto);
        return ResponseEntity.ok(
                Response.<Student>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật người dùng thành công")
                        .data(user)
                        .build()
        );
    }

    @DeleteMapping(path = "/{studentId}")
    @RequiredRole({Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable String studentId) {
        studentService.delete(studentId);
        return ResponseEntity.ok(
                Response.<Student>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa người dùng thành công")
                        .build()
        );
    }
}
