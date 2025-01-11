package com.ht.qlktx.modules.discount;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Discount;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.discount.dtos.CreateDiscountDto;
import com.ht.qlktx.modules.discount.dtos.UpdateDiscountDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/discount")
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Discount>> create(@Valid @RequestBody CreateDiscountDto createDiscountDto,
                                                     @RequestAttribute("sub") String sub) {
        var discount = discountService.create(createDiscountDto, sub);
        return ResponseEntity.created(null).body(new Response<>(
                HttpStatus.CREATED.value(),
                "Mã giảm giá đã được tạo",
                discount
        ));
    }

    @PutMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Discount>> update(@PathVariable String id,
                                                     @Valid @RequestBody UpdateDiscountDto updateDiscountDto) {
        var discount = discountService.update(id, updateDiscountDto);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Mã giảm giá đã được cập nhật",
                discount
        ));
    }

    @DeleteMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<String>> delete(@PathVariable String id) {
        discountService.delete(id);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Mã giảm giá đã được xóa",
                null
        ));
    }

    @GetMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Discount>> findById(@PathVariable String id) {
        var discount = discountService.findById(id);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Mã giảm giá đã được tìm thấy",
                discount
        ));
    }

    @GetMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Discount>>> findAll() {
        var discounts = discountService.findAll();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách mã giảm giá",
                discounts
        ));
    }

    @GetMapping("/available")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Iterable<Discount>>> findAllAvailable() {
        var discounts = discountService.findAllAvailable();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Danh sách mã giảm giá",
                discounts
        ));
    }
}
