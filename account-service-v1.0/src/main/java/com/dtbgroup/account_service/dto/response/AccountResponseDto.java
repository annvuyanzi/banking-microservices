package com.dtbgroup.account_service.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AccountResponseDto {
    private String accountId;
    private String iban;
    private String bicSwift;
    private String  customerId;
    private  String status;
    private boolean isEnabled ;
}
