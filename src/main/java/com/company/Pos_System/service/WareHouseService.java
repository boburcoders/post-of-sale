package com.company.Pos_System.service;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.WareHouseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WareHouseService {
    HttpApiResponse<WareHouseDto> createWareHouse(WareHouseDto dto);

    HttpApiResponse<WareHouseDto> getWareHouseById(Long id);

    HttpApiResponse<List<WareHouseDto>> getAllWareHouse();

    HttpApiResponse<WareHouseDto> updateWareHouseById(Long id, WareHouseDto dto);

    HttpApiResponse<String> deleteWareHouseById(Long id);

}
