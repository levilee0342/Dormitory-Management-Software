package com.ht.qlktx.modules.discount;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Discount;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.discount.dtos.CreateDiscountDto;
import com.ht.qlktx.modules.discount.dtos.UpdateDiscountDto;
import com.ht.qlktx.modules.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final BookingRepository bookingRepository;
    private final StaffService staffService;

    public Discount create(CreateDiscountDto createDiscountDto, String sub) {
        if (discountRepository.existsByIdAndDeletedIsFalse(createDiscountDto.getId()) || discountRepository.existsByDescriptionAndDeletedIsFalse(createDiscountDto.getDescription()))
            throw new HttpException("Mã giảm giá hoặc mô tả đã tồn tại", HttpStatus.BAD_REQUEST);

        if (createDiscountDto.getStartDate().after(createDiscountDto.getEndDate()) || createDiscountDto.getStartDate().equals(createDiscountDto.getEndDate()))
            throw new HttpException("Ngày kết thúc phải sau ngày bắt đầu", HttpStatus.BAD_REQUEST);

        Discount discount = Discount.builder()
                .id(createDiscountDto.getId())
                .description(createDiscountDto.getDescription())
                .percentage(createDiscountDto.getPercentage())
                .staff(staffService.findById(sub))
                .startDate(createDiscountDto.getStartDate())
                .endDate(createDiscountDto.getEndDate())
                .build();

        return discountRepository.save(discount);
    }

    public Discount findById(String id) {
        return discountRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new HttpException("Không tìm thấy mã giảm giá", HttpStatus.NOT_FOUND));
    }

    public Discount findByAvailableId(String id) {
        return discountRepository.findByIdAndDeletedIsFalseAndStartDateBeforeAndEndDateAfter(id, new Date(), new Date())
                .orElseThrow(() -> new HttpException("Không tìm thấy mã giảm giá", HttpStatus.NOT_FOUND));
    }

    public Discount update(String id, UpdateDiscountDto updateDiscountDto) {
        Discount discount = findById(id);

        Optional.ofNullable(updateDiscountDto.getDescription()).ifPresent(discount::setDescription);
        Optional.ofNullable(updateDiscountDto.getPercentage()).ifPresent(discount::setPercentage);
        Optional.ofNullable(updateDiscountDto.getStartDate()).ifPresent(discount::setStartDate);
        Optional.ofNullable(updateDiscountDto.getEndDate()).ifPresent(discount::setEndDate);

        if (discount.getStartDate().after(discount.getEndDate()) || discount.getStartDate().equals(discount.getEndDate()))
            throw new HttpException("Ngày kết thúc phải sau ngày bắt đầu", HttpStatus.BAD_REQUEST);

        return discountRepository.save(discount);
    }

    public void delete(String id) {
        if (bookingRepository.existsByDiscountId(id))
            throw new HttpException("Không thể xoá vì mã giảm giá đã được sử dụng", HttpStatus.BAD_REQUEST);
        Discount discount = findById(id);
        discount.setDeleted(true);
        discountRepository.save(discount);
    }

    public List<Discount> findAll() {
        return discountRepository.findAllByDeletedIsFalse();
    }

    public List<Discount> findAllAvailable() {
        return discountRepository.findAllByDeletedIsFalseAndStartDateBeforeAndEndDateAfter(new Date(), new Date());
    }
}
