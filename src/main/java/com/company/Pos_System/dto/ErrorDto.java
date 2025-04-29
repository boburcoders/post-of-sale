package com.company.Pos_System.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ErrorDto {
    private String errorPath;
    private Integer errorCode;
    private Object errorBody;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
