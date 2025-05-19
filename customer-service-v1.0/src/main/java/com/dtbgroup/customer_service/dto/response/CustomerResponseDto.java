package com.dtbgroup.customer_service.dto.response;

import lombok.Data;

@Data
public class CustomerResponseDto {
    private String customerId;
    private String firstName;
    private String lastName;
    private String otherName;
    private  String status;
    private boolean isEnabled;
}
