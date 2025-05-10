package com.company.Pos_System.service.impl;

import com.company.Pos_System.dto.HttpApiResponse;
import com.company.Pos_System.dto.WareHouseDto;
import com.company.Pos_System.exceptions.ItemNotFoundException;
import com.company.Pos_System.models.WareHouse;
import com.company.Pos_System.repository.WareHouseRepository;
import com.company.Pos_System.service.mapper.WareHouseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WareHouseServiceImplTest {

    @Mock
    WareHouseRepository wareHouseRepository;
    @Mock
    WareHouseMapper wareHouseMapper;

    WareHouseServiceImpl wareHouseService;

    WareHouse wareHouse;
    WareHouseDto wareHouseDto;


    @BeforeEach
    void setUp() {
        wareHouse = WareHouse.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseDto = WareHouseDto.builder()
                .name("Warehouse Name")
                .location("Location")
                .build();
        wareHouseService = new WareHouseServiceImpl(wareHouseRepository, wareHouseMapper);

    }

    @Test
    void createWareHouseForSuccessTest() {
        when(wareHouseMapper.toEntity(wareHouseDto)).thenReturn(wareHouse);
        when(wareHouseRepository.save(wareHouse)).thenReturn(wareHouse);
        when(wareHouseMapper.toDto(wareHouse)).thenReturn(wareHouseDto);

        HttpApiResponse<WareHouseDto> response = wareHouseService.createWareHouse(wareHouseDto);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.CREATED, response.getStatus());
        assertEquals("WareHouse created successfully", response.getMessage());
        assertEquals(wareHouseDto, response.getData());

        verify(wareHouseRepository, times(1)).save(wareHouse);
        verify(wareHouseMapper, times(1)).toEntity(wareHouseDto);
        verify(wareHouseMapper, times(1)).toDto(wareHouse);


    }

    @Test
    void getWareHouseByIdForSuccessTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(wareHouse));
        when(wareHouseMapper.toDto(wareHouse)).thenReturn(wareHouseDto);

        HttpApiResponse<WareHouseDto> response = wareHouseService.getWareHouseById(1L);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(wareHouseDto, response.getData());

        verify(wareHouseRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(wareHouseMapper, times(1)).toDto(wareHouse);

    }

    @Test
    void getWareHouseByIdForFailureTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("WareHouse not found"));

        assertThrows(ItemNotFoundException.class, () -> wareHouseService.getWareHouseById(1L));

        verifyNoInteractions(wareHouseMapper);
    }

    @Test
    void getAllWareHouseForSuccessTest() {
        when(wareHouseRepository.findAllByDeletedAtIsNull()).thenReturn(Optional.of(List.of(wareHouse)));
        when(wareHouseMapper.toDtoList(List.of(wareHouse))).thenReturn(List.of(wareHouseDto));

        HttpApiResponse<List<WareHouseDto>> response = wareHouseService.getAllWareHouse();
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("OK", response.getMessage());
        assertEquals(List.of(wareHouseDto), response.getData());

        verify(wareHouseRepository, times(1)).findAllByDeletedAtIsNull();
        verify(wareHouseMapper, times(1)).toDtoList(List.of(wareHouse));
    }

    @Test
    void getAllWareHouseForFailureTest() {
        when(wareHouseRepository.findAllByDeletedAtIsNull()).thenThrow(new ItemNotFoundException("WareHouses not found"));
        assertThrows(ItemNotFoundException.class, () -> wareHouseService.getAllWareHouse());
        verifyNoInteractions(wareHouseMapper);
    }

    @Test
    void updateWareHouseByIdForSuccessTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(wareHouse));
        when(wareHouseMapper.updateEntity(wareHouse, wareHouseDto)).thenReturn(wareHouse);
        when(wareHouseRepository.save(wareHouse)).thenReturn(wareHouse);
        when(wareHouseMapper.toDto(wareHouse)).thenReturn(wareHouseDto);

        HttpApiResponse<WareHouseDto> response = wareHouseService.updateWareHouseById(1L, wareHouseDto);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("WareHouse updated successfully", response.getMessage());
        assertEquals(wareHouseDto, response.getData());

        verify(wareHouseRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(wareHouseMapper, times(1)).updateEntity(wareHouse, wareHouseDto);
        verify(wareHouseRepository, times(1)).save(wareHouse);
        verify(wareHouseMapper, times(1)).toDto(wareHouse);
    }

    @Test
    void updateWareHouseByIdForFailureTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("WareHouse not found"));
        assertThrows(ItemNotFoundException.class, () -> wareHouseService.updateWareHouseById(1L, wareHouseDto));
        verifyNoInteractions(wareHouseMapper);
    }

    @Test
    void deleteWareHouseByIdForSuccessTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(wareHouse));
        when(wareHouseRepository.save(wareHouse)).thenReturn(wareHouse);

        HttpApiResponse<String> response = wareHouseService.deleteWareHouseById(1L);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals("WareHouse deleted successfully", response.getMessage());

        verify(wareHouseRepository, times(1)).findByIdAndDeletedAtIsNull(1L);
        verify(wareHouseRepository, times(1)).save(wareHouse);
    }

    @Test
    void deleteWareHouseByIdForFailureTest() {
        when(wareHouseRepository.findByIdAndDeletedAtIsNull(1L)).thenThrow(new ItemNotFoundException("WareHouse not found"));
        assertThrows(ItemNotFoundException.class, () -> wareHouseService.deleteWareHouseById(1L));
        verifyNoInteractions(wareHouseMapper);

    }
}