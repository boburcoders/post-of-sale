package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.exceptions.ItemNotFoundException;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.repository.WareHouseRepository;
import com.company.Pos_System.service.WareHouseService;
import com.company.Pos_System.service.mapper.WareHouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final WareHouseRepository wareHouseRepository;
    private final WareHouseMapper wareHouseMapper;

    @Override
    public HttpApiResponse<WareHouseDto> createWareHouse(WareHouseDto dto) {
        try {
            return HttpApiResponse.<WareHouseDto>builder()
                    .success(true)
                    .status(HttpStatus.CREATED)
                    .message("WareHouse created successfully")
                    .data(wareHouseMapper.toDto(wareHouseRepository.save(wareHouseMapper.toEntity(dto))))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public HttpApiResponse<WareHouseDto> getWareHouseById(Long id) {
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("WareHouse not found with id: " + id));

        WareHouseDto dto = wareHouseMapper.toDto(wareHouse);

        return HttpApiResponse.<WareHouseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(dto)
                .build();
    }

    @Override
    public HttpApiResponse<List<WareHouseDto>> getAllWareHouse() {
        List<WareHouse> wareHouseList = wareHouseRepository.findAllByDeletedAtIsNull()
                .orElseThrow(() -> new ItemNotFoundException("WareHouses not found"));

        List<WareHouseDto> wareHouseDtoList = wareHouseMapper.toDtoList(wareHouseList);

        return HttpApiResponse.<List<WareHouseDto>>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("OK")
                .data(wareHouseDtoList)
                .build();
    }

    @Override
    public HttpApiResponse<WareHouseDto> updateWareHouseById(Long id, WareHouseDto dto) {

        WareHouse wareHouse = wareHouseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("WareHouse not found with id: " + id));

        WareHouse updatedEntity = wareHouseMapper.updateEntity(wareHouse, dto);

        wareHouseRepository.save(updatedEntity);

        return HttpApiResponse.<WareHouseDto>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("WareHouse updated successfully")
                .data(wareHouseMapper.toDto(updatedEntity))
                .build();
    }

    @Override
    public HttpApiResponse<String> deleteWareHouseById(Long id) {
        WareHouse wareHouse = wareHouseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ItemNotFoundException("WareHouse not found with id: " + id));
        wareHouse.setDeletedAt(LocalDateTime.now());
        wareHouseRepository.save(wareHouse);

        return HttpApiResponse.<String>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("WareHouse deleted successfully")
                .build();
    }
}
