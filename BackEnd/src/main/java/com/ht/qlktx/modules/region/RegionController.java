package com.ht.qlktx.modules.region;

import com.ht.qlktx.annotations.RequiredRole;
import com.ht.qlktx.config.Response;
import com.ht.qlktx.entities.Region;
import com.ht.qlktx.enums.Role;
import com.ht.qlktx.modules.region.dtos.CreateRegionDto;
import com.ht.qlktx.modules.region.dtos.UpdateRegionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping(path = "/api/v1/regions")
public class RegionController {
    private final RegionService regionService;

    @PostMapping
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Region>> create(@Valid @RequestBody CreateRegionDto createRegionDto) {
        var region = regionService.create(createRegionDto);
        return ResponseEntity.created(null).body(
                Response.<Region>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tạo dãy phòng thành công")
                        .data(region)
                        .build()
        );
    }

    @PutMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<Region>> update(@PathVariable String id, @Valid @RequestBody UpdateRegionDto updateRegionDto) {
        var region = regionService.update(id, updateRegionDto);
        return ResponseEntity.ok(
                Response.<Region>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cập nhật dãy phòng thành công")
                        .data(region)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Response<List<Region>>> findAll() {
        var regions = regionService.findAll();
        return ResponseEntity.ok(
                Response.<List<Region>>builder()
                        .status(HttpStatus.OK.value())
                        .message("ok")
                        .data(regions)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Region>> findById(@PathVariable String id) {
        var region = regionService.findById(id);
        return ResponseEntity.ok(
                Response.<Region>builder()
                        .status(HttpStatus.OK.value())
                        .message("ok")
                        .data(region)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @RequiredRole({Role.STAFF, Role.ADMIN})
    public ResponseEntity<Response<?>> delete(@PathVariable String id) {
        regionService.delete(id);
        return ResponseEntity.ok(
                Response.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Xóa dãy phòng thành công")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/lookup")
    public ResponseEntity<Response<List<Region>>> lookUpById(@RequestParam String keyword) {
        var regions = regionService.lookUpById(keyword);
        return ResponseEntity.ok(
                Response.<List<Region>>builder()
                        .status(HttpStatus.OK.value())
                        .message("ok")
                        .data(regions)
                        .build()
        );
    }
}
