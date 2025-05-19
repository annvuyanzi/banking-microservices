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
public class AccountApiResponseDto {
        private String statusCode;
        private String statusDescription;
        private String messageCode;
        private String messageDescription;
        private Object errorInfo;
        private PrimaryData primaryData;

    @Data
    public class PrimaryData {
        private int totalItems;
        private int totalPages;
        private int currentPage;
        private List<Object> content;
    }
}
