package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.models.WareHouse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-12T11:19:48+0500",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 17.0.14 (Amazon.com Inc.)"
)
@Component
public class WareHouseMapperImpl extends WareHouseMapper {

    @Override
    public WareHouseDto toDto(WareHouse entity) {
        if ( entity == null ) {
            return null;
        }

        WareHouseDto.WareHouseDtoBuilder wareHouseDto = WareHouseDto.builder();

        wareHouseDto.id( entity.getId() );
        wareHouseDto.name( entity.getName() );
        wareHouseDto.location( entity.getLocation() );

        return wareHouseDto.build();
    }

    @Override
    public WareHouse toEntity(WareHouseDto dto) {
        if ( dto == null ) {
            return null;
        }

        WareHouse.WareHouseBuilder wareHouse = WareHouse.builder();

        wareHouse.name( dto.getName() );
        wareHouse.location( dto.getLocation() );

        return wareHouse.build();
    }

    @Override
    public WareHouse updateEntity(WareHouse entity, WareHouseDto dto) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getLocation() != null ) {
            entity.setLocation( dto.getLocation() );
        }

        return entity;
    }

    @Override
    public List<WareHouseDto> toDtoList(List<WareHouse> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<WareHouseDto> list = new ArrayList<WareHouseDto>( entityList.size() );
        for ( WareHouse wareHouse : entityList ) {
            list.add( toDto( wareHouse ) );
        }

        return list;
    }
}
