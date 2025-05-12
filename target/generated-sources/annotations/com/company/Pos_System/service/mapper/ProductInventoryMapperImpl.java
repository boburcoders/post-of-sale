package com.company.Pos_System.service.mapper;

import com.company.Pos_System.dto.ProductInventoryDto;
import com.company.Pos_System.models.Product;
import com.company.Pos_System.models.ProductInventory;
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
public class ProductInventoryMapperImpl extends ProductInventoryMapper {

    @Override
    public ProductInventoryDto toDto(ProductInventory entity) {
        if ( entity == null ) {
            return null;
        }

        ProductInventoryDto.ProductInventoryDtoBuilder productInventoryDto = ProductInventoryDto.builder();

        productInventoryDto.productId( entityProductId( entity ) );
        productInventoryDto.warehouseId( entityWarehouseId( entity ) );
        productInventoryDto.id( entity.getId() );
        productInventoryDto.quantity( entity.getQuantity() );

        return productInventoryDto.build();
    }

    @Override
    public ProductInventory toEntity(ProductInventoryDto dto) {
        if ( dto == null ) {
            return null;
        }

        ProductInventory.ProductInventoryBuilder productInventory = ProductInventory.builder();

        productInventory.quantity( dto.getQuantity() );

        return productInventory.build();
    }

    @Override
    public ProductInventory updateEntity(ProductInventory entity, ProductInventoryDto dto) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        entity.setQuantity( dto.getQuantity() );

        return entity;
    }

    @Override
    public List<ProductInventoryDto> toDtoList(List<ProductInventory> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<ProductInventoryDto> list = new ArrayList<ProductInventoryDto>( entityList.size() );
        for ( ProductInventory productInventory : entityList ) {
            list.add( toDto( productInventory ) );
        }

        return list;
    }

    private Long entityProductId(ProductInventory productInventory) {
        Product product = productInventory.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private Long entityWarehouseId(ProductInventory productInventory) {
        WareHouse warehouse = productInventory.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }
}
