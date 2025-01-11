package com.ht.qlktx.modules.region.dtos;

import com.ht.qlktx.enums.Sex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRegionDto {
    private String name;
    private Sex sex;
}
