package com.ht.qlktx.modules.region;

import com.ht.qlktx.config.HttpException;
import com.ht.qlktx.entities.Region;
import com.ht.qlktx.modules.booking.repositories.BookingRepository;
import com.ht.qlktx.modules.region.dtos.CreateRegionDto;
import com.ht.qlktx.modules.region.dtos.UpdateRegionDto;
import com.ht.qlktx.modules.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    public Region findById(String id) {
        return regionRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new HttpException("Không tìm thấy dãy phòng với mã phòng này", HttpStatus.NOT_FOUND));
    }

    public List<Region> findAll() {
        return regionRepository.findAllByDeletedIsFalse();
    }

    public Region create(CreateRegionDto createRegionDto) {
        if (regionRepository.existsByIdAndDeletedIsFalse(createRegionDto.getId()) || regionRepository.existsByNameAndDeletedIsFalse(createRegionDto.getName())) {
            throw new HttpException("Dãy phòng hoặc tên dãy phòng đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        var region = Region.builder()
                .id(createRegionDto.getId())
                .name(createRegionDto.getName())
                .sex(createRegionDto.getSex())
                .build();

        return regionRepository.save(region);
    }

    public void delete(String id) {
        var region = findById(id);
        if (roomRepository.existsByRegionIdAndDeletedIsFalse(id)) {
            throw new HttpException("Không thể xóa dãy phòng này vì có phòng trong dãy phòng", HttpStatus.BAD_REQUEST);
        }
        region.setDeleted(true);
        regionRepository.save(region);
    }

    public List<Region> lookUpById(String keyword) {
        return regionRepository.findByIdContainingIgnoreCaseAndDeletedIsFalse(keyword);
    }

    public Region update(String id, UpdateRegionDto updateRegionDto) {
        var region = findById(id);

        Optional.ofNullable(updateRegionDto.getName()).ifPresent(region::setName);
        Optional.ofNullable(updateRegionDto.getSex()).ifPresent(sex -> {
            var hasBooking = bookingRepository.existsByRoomRegionIdAndDeletedIsFalse(id);
            if (hasBooking) {
                throw new HttpException("Không thể thay đổi giới tính vì đã có người thuê", HttpStatus.BAD_REQUEST);
            }
            region.setSex(sex);
        });

        return regionRepository.save(region);
    }
}
