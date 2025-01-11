package com.ht.qlktx.modules.room_type;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.RoomType;
import com.ht.qlktx.modules.room.RoomRepository;
import com.ht.qlktx.modules.room.RoomService;
import com.ht.qlktx.modules.room_type.dtos.CreateRoomTypeDto;
import com.ht.qlktx.modules.room_type.dtos.UpdateRoomTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    public RoomType create(CreateRoomTypeDto createRoomTypeDto) {
        if (roomTypeRepository.existsByNameAndDeletedIsFalse(createRoomTypeDto.getName())) {
            throw new HttpException("Loại phòng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var deletedRoomType = roomTypeRepository.findByNameAndDeletedIsTrue(createRoomTypeDto.getName()).orElse(null);

        if (deletedRoomType != null) {
            deletedRoomType.setDeleted(false);
            deletedRoomType.setPrice(createRoomTypeDto.getPrice());
            deletedRoomType.setCapacity(createRoomTypeDto.getCapacity());
            return roomTypeRepository.save(deletedRoomType);
        }

        var roomType = RoomType.builder()
                .name(createRoomTypeDto.getName())
                .price(createRoomTypeDto.getPrice())
                .capacity(createRoomTypeDto.getCapacity())
                .build();

        return roomTypeRepository.save(roomType);
    }

    public List<RoomType> findAll() {
        return roomTypeRepository.findAllByDeletedIsFalse();
    }

    public RoomType findById(Long id) {
        return roomTypeRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new HttpException("Không tìm thấy loại phòng", HttpStatus.BAD_REQUEST));
    }

    public void delete(Long id) {
        var roomType = findById(id);

        if (roomRepository.existsByTypeIdAndDeletedFalse(id)) {
            throw new HttpException("Không thể xóa loại phòng này vì có phòng đang sử dụng", HttpStatus.BAD_REQUEST);
        }

        roomType.setDeleted(true);
        roomTypeRepository.save(roomType);
    }

    public RoomType update(Long id, UpdateRoomTypeDto updateRoomTypeDto) {
        var roomType = findById(id);

        Optional.ofNullable(updateRoomTypeDto.getName()).ifPresent(roomType::setName);
        Optional.ofNullable(updateRoomTypeDto.getPrice()).ifPresent(roomType::setPrice);
        Optional.of(updateRoomTypeDto.getCapacity()).ifPresent(roomType::setCapacity);

        if (roomTypeRepository.existsByNameAndIdNot(updateRoomTypeDto.getName(), id)) {
            throw new HttpException("Loại phòng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        return roomTypeRepository.save(roomType);
    }

    public List<RoomType> lookUpByName(String keyword) {
        return roomTypeRepository.findByNameContainingIgnoreCaseAndDeletedIsFalse(keyword);
    }
}
