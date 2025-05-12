package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.models.WareHouse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class WareHouseMapper {

    @Named("toDto")
    public abstract WareHouseDto toDto(WareHouse entity);

    public abstract WareHouse toEntity(WareHouseDto dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract WareHouse updateEntity(@MappingTarget WareHouse entity, WareHouseDto dto);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<WareHouseDto> toDtoList(List<WareHouse> entityList);

}
