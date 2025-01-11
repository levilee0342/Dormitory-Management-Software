package com.ht.qlktx.modules.room_type;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.room_type.dtos.CreateRoomTypeDto;
import com.ht.qlktx.modules.room_type.dtos.UpdateRoomTypeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/room-types")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<RoomType>> create(@Valid @RequestBody CreateRoomTypeDto createRoomTypeDto) {
        var roomType = roomTypeService.create(createRoomTypeDto);
        return ResponseEntity.created(null).body(new Response<>(
                HttpStatus.CREATED.value(),
                "Tạo loại phòng thành công",
                roomType
        ));
    }

    @GetMapping
    public ResponseEntity<Response<Iterable<RoomType>>> findAll() {
        var roomTypes = roomTypeService.findAll();
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Lấy danh sách loại phòng thành công",
                roomTypes
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<RoomType>> findById(@PathVariable Long id) {
        var roomType = roomTypeService.findById(id);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Lấy loại phòng thành công",
                roomType
        ));
    }

    @DeleteMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable Long id) {
        roomTypeService.delete(id);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Xóa loại phòng thành công",
                null
        ));
    }

    @PutMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<RoomType>> update(@PathVariable Long id, @Valid @RequestBody UpdateRoomTypeDto updateRoomTypeDto) {
        var roomType = roomTypeService.update(id, updateRoomTypeDto);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Cập nhật loại phòng thành công",
                roomType
        ));
    }

    @GetMapping("/lookup")
    public ResponseEntity<Response<Iterable<RoomType>>> lookUpByName(@RequestParam String keyword) {
        var roomTypes = roomTypeService.lookUpByName(keyword);
        return ResponseEntity.ok().body(new Response<>(
                HttpStatus.OK.value(),
                "Lấy danh sách loại phòng thành công",
                roomTypes
        ));
    }
}
