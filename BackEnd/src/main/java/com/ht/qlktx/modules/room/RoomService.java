package com.ht.qlktx.modules.room;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Room;
import com.ht.qlktx.enums.RoomStatus;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.region.RegionService;
import com.ht.qlktx.modules.room.dtos.CreateRoomDto;
import com.ht.qlktx.modules.room.dtos.UpdateRoomDto;
import com.ht.qlktx.modules.room_type.RoomTypeService;
import com.ht.qlktx.projections.RoomDetailView;
import com.ht.qlktx.projections.RoomWithBookingCountView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RoomTypeService roomTypeService;
    private final RegionService regionService;

    public Room create(CreateRoomDto createRoomDto) {
        if (roomRepository.existsByIdAndDeletedIsFalse(createRoomDto.getId())) {
            throw new HttpException("Mã phòng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var room = Room.builder()
                .id(createRoomDto.getId())
                .type(roomTypeService.findById(createRoomDto.getTypeId()))
                .region(regionService.findById(createRoomDto.getRegionId()))
                .deleted(false)
                .status(RoomStatus.AVAILABLE)
                .build();

        return roomRepository.save(room);
    }

    public void delete(String roomId) {
        var room = findById(roomId);

        if (bookingRepository.existsByRoomIdAndDeletedIsFalse(roomId)) {
            throw new HttpException("Không thể xoá phòng đã có phiếu thuê", HttpStatus.BAD_REQUEST);
        }

        room.setDeleted(true);
        roomRepository.save(room);
    }

    public void update(String roomId, UpdateRoomDto updateRoomDto) {
        var room = findById(roomId);

        if (bookingRepository.existsByRoomIdAndDeletedIsFalseAndCheckedOutAtIsNull(roomId)) {
            throw new HttpException("Không thể cập nhật phòng đang có người lưu trú", HttpStatus.BAD_REQUEST);
        }

        Optional.ofNullable(updateRoomDto.getRegionId()).ifPresent(regionId -> {
            room.setRegion(regionService.findById(regionId));
        });

        Optional.ofNullable(updateRoomDto.getTypeId()).ifPresent(typeId -> {
            room.setType(roomTypeService.findById(typeId));
        });

        Optional.ofNullable(updateRoomDto.getStatus()).ifPresent(room::setStatus);

        roomRepository.save(room);
    }

    public List<RoomWithBookingCountView> findAll() {
        return roomRepository.findAllWithBookingCount();
    }

    public Room findById(String roomId) {
        return roomRepository.findByIdAndDeletedFalse(roomId).orElseThrow(
                () -> new HttpException("Không tìm thấy phòng", HttpStatus.BAD_REQUEST)
        );
    }

    public <T> T findById(String roomId, Class<T> tClass) {
        return roomRepository.findByIdAndDeletedFalse(roomId, tClass).orElseThrow(
                () -> new HttpException("Không tìm thấy phòng", HttpStatus.BAD_REQUEST)
        );
    }

    public List<Room> lookUpById(String keyword) {
        return roomRepository.findByIdContainingIgnoreCaseAndDeletedFalse(keyword);
    }
}
