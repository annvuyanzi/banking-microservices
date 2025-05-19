package com.dtbgroup.account_service.dto;

import lombok.Data;

@Data
public class AccountDto {
    private String iban;
    private String bicSwift;
    private String  customerId;
}
