package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.models.ProductInventory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductInventoryMapper {

    @Named("toDto")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    public abstract ProductInventoryDto toDto(ProductInventory entity);


    public abstract ProductInventory toEntity(ProductInventoryDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    public abstract ProductInventory updateEntity(@MappingTarget ProductInventory entity, ProductInventoryDto dto);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract List<ProductInventoryDto> toDtoList(List<ProductInventory> entityList);



}
