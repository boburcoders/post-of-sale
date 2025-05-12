package com.company.Pos_System.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpApiResponse<T> {
    private HttpStatus status; //int good
    private String message;
    private boolean success;
    private T data;

}
