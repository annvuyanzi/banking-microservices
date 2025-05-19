package com.dtbgroup.card_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private static final long serialVersionUID = -7857624541088572239L;
    private String statusCode;
    private String statusDescription;
    private String messageCode;
    private String messageDescription;
    private List<String> errorInfo;
    private Object primaryData;
}
