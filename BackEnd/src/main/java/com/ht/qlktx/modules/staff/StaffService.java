package com.ht.qlktx.modules.staff;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Staff;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.account.AccountRepository;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.booking.repositories.BookingTimeRepository;
import com.ht.qlktx.modules.discount.DiscountRepository;
import com.ht.qlktx.modules.staff.dtos.CreateStaffDto;
import com.ht.qlktx.modules.staff.dtos.UpdateProfileDto;
import com.ht.qlktx.modules.staff.dtos.UpdateStaffDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final BookingRepository bookingRepository;
    private final BookingTimeRepository bookingTimeRepository;
    private final DiscountRepository discountRepository;

    public List<Staff> findAll() {
        return staffRepository.findAllByDeletedIsFalse();
    }

    public Staff findById(String staffId) {
        return staffRepository.findByIdAndDeletedIsFalse(staffId).orElseThrow(
                () -> new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST)
        );
    }

    public Staff findByAccount(String accountId) {
        return staffRepository.findByAccountAndDeletedIsFalse(accountId).orElseThrow(
                () -> new HttpException("Không tìm thấy người dùng", HttpStatus.BAD_REQUEST)
        );
    }

    public boolean existsByAccountId(String accountId) {
        return staffRepository.existsByAccountUsernameAndDeletedIsFalse(accountId);
    }

    @Transactional
    public Staff create(CreateStaffDto createStaffDto) {
        if (staffRepository.existsByIdAndDeletedIsFalse(createStaffDto.getId())) {
            throw new HttpException("Mã người dùng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var staff = Staff.builder()
                .id(createStaffDto.getId())
                .firstName(createStaffDto.getFirstName())
                .lastName(createStaffDto.getLastName())
                .sex(createStaffDto.getSex())
                .dateOfBirth(createStaffDto.getDateOfBirth())
                .address(createStaffDto.getAddress())
                .phone(createStaffDto.getPhone())
                .build();

        return staffRepository.save(staff);
    }

    public Staff update(String staffId, UpdateStaffDto updateStaffDto) {
        var staff = findById(staffId);

        Optional.ofNullable(updateStaffDto.getFirstName()).ifPresent(staff::setFirstName);
        Optional.ofNullable(updateStaffDto.getLastName()).ifPresent(staff::setLastName);
        Optional.ofNullable(updateStaffDto.getSex()).ifPresent(staff::setSex);
        Optional.ofNullable(updateStaffDto.getDateOfBirth()).ifPresent(staff::setDateOfBirth);
        Optional.ofNullable(updateStaffDto.getAddress()).ifPresent(staff::setAddress);
        Optional.ofNullable(updateStaffDto.getPhone()).ifPresent(staff::setPhone);

        return staffRepository.save(staff);
    }

    public Staff update(String staffId, UpdateProfileDto updateProfileDto) {
        var staff = findById(staffId);

        Optional.ofNullable(updateProfileDto.getFirstName()).ifPresent(staff::setFirstName);
        Optional.ofNullable(updateProfileDto.getLastName()).ifPresent(staff::setLastName);
        Optional.ofNullable(updateProfileDto.getSex()).ifPresent(staff::setSex);
        Optional.ofNullable(updateProfileDto.getDateOfBirth()).ifPresent(staff::setDateOfBirth);
        Optional.ofNullable(updateProfileDto.getAddress()).ifPresent(staff::setAddress);
        Optional.ofNullable(updateProfileDto.getPhone()).ifPresent(staff::setPhone);

        return staffRepository.save(staff);
    }

    public void delete(String staffId) {
        var staff = findById(staffId);

        var hasBooking = bookingRepository.existsByCheckinStaffIdOrCheckoutStaffId(staffId, staffId);

        if (hasBooking)
            throw new HttpException("Không thể xóa vì nhân viên đã tiếp nhận phiếu thuê", HttpStatus.BAD_REQUEST);

        var hasBookingTime = bookingTimeRepository.existsByStaffId(staffId);

        if (hasBookingTime)
            throw new HttpException("Không thể xóa vì nhân viên đã tạo thời gian thuê", HttpStatus.BAD_REQUEST);

        var hasDiscount = discountRepository.existsByStaffId(staffId);

        if (hasDiscount)
            throw new HttpException("Không thể xóa vì nhân viên đã tạo mã giảm giá", HttpStatus.BAD_REQUEST);

        if (staff.getAccount() != null && staff.getAccount().getRole().getRole().equals(Role.ADMIN.toString())) {
            throw new HttpException("Không thể xóa người dùng với quyền ADMIN", HttpStatus.FORBIDDEN);
        }

        staff.setDeleted(true);
        staff.setAccount(null);
        staffRepository.save(staff);
    }

    public List<Staff> lookUpByIdOrName(String keyword) {
        return staffRepository.lookUpByIdOrName(keyword);
    }

    public void save(Staff staff) {
        staffRepository.save(staff);
    }

    public List<Staff> findAllNonAccount() {
        return staffRepository.findAllByAccountIsNullAndDeletedIsFalse();
    }
}
