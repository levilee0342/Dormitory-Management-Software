package com.ht.qlktx.modules.student;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Student;
import com.ht.qlktx.modules.account.AccountRepository;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.student.dtos.CreateStudentDto;
import com.ht.qlktx.modules.student.dtos.UpdateProfileDto;
import com.ht.qlktx.modules.student.dtos.UpdateStudentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final BookingRepository bookingRepository;

    public List<Student> findAll() {
        return studentRepository.findAllByDeletedIsFalse();
    }

    public List<Student> lookupByIdOrName(String keyword) {
        return studentRepository.lookupByIdOrName(keyword);
    }

    public Student findById(String studentId) {
        return studentRepository.findByIdAndDeletedIsFalse(studentId).orElseThrow(
                () -> new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST)
        );
    }

    public Student findByAccount(String accountId) {
        return studentRepository.findByAccountAndDeletedIsFalse(accountId).orElseThrow(
                () -> new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST)
        );
    }

    public boolean existsByAccountId(String accountId) {
        return studentRepository.existsByAccountUsernameAndDeletedIsFalse(accountId);
    }

    @Transactional
    public Student create(CreateStudentDto createStudentDto) {
        if (studentRepository.existsByIdAndDeletedIsFalse(createStudentDto.getId())) {
            throw new HttpException("Mã người dùng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var student = Student.builder()
                .id(createStudentDto.getId())
                .firstName(createStudentDto.getFirstName())
                .lastName(createStudentDto.getLastName())
                .sex(createStudentDto.getSex())
                .dateOfBirth(createStudentDto.getDateOfBirth())
                .address(createStudentDto.getAddress())
                .phone(createStudentDto.getPhone())
                .build();


        return studentRepository.save(student);
    }

    public Student update(String studentId, UpdateStudentDto updateStudentDto) {
        var student = findById(studentId);

        Optional.ofNullable(updateStudentDto.getFirstName()).ifPresent(student::setFirstName);
        Optional.ofNullable(updateStudentDto.getLastName()).ifPresent(student::setLastName);
        Optional.ofNullable(updateStudentDto.getSex()).ifPresent(student::setSex);
        Optional.ofNullable(updateStudentDto.getDateOfBirth()).ifPresent(student::setDateOfBirth);
        Optional.ofNullable(updateStudentDto.getAddress()).ifPresent(student::setAddress);
        Optional.ofNullable(updateStudentDto.getPhone()).ifPresent(student::setPhone);

        return studentRepository.save(student);
    }

    public Student update(String studentId, UpdateProfileDto updateProfileDto) {
        var student = findById(studentId);

        Optional.ofNullable(updateProfileDto.getFirstName()).ifPresent(student::setFirstName);
        Optional.ofNullable(updateProfileDto.getLastName()).ifPresent(student::setLastName);
        Optional.ofNullable(updateProfileDto.getSex()).ifPresent(student::setSex);
        Optional.ofNullable(updateProfileDto.getDateOfBirth()).ifPresent(student::setDateOfBirth);
        Optional.ofNullable(updateProfileDto.getAddress()).ifPresent(student::setAddress);
        Optional.ofNullable(updateProfileDto.getPhone()).ifPresent(student::setPhone);

        return studentRepository.save(student);
    }

    public void delete(String studentId) {
        var student = findById(studentId);

        var hasBooking = bookingRepository.existsByStudentId(studentId);

        if (hasBooking)
            throw new HttpException("Không thể xoá vì sinh viên đã có phiếu thuê", HttpStatus.BAD_REQUEST);

        student.setDeleted(true);
        student.setAccount(null);
        studentRepository.save(student);
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public List<Student> findAllNonAccount() {
        return studentRepository.findAllByAccountIsNullAndDeletedIsFalse();
    }
}
