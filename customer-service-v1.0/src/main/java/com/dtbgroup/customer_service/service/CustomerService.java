package com.dtbgroup.customer_service.service;

import com.dtbgroup.customer_service.dto.ApiResponseDto;
import com.dtbgroup.customer_service.dto.CustomerInputDto;
import com.dtbgroup.customer_service.dto.response.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface CustomerService {
    ApiResponseDto createCustomer(CustomerInputDto dto);
    ApiResponseDto getCustomerById(String customerId, Pageable pageable);
    ApiResponseDto getAllCustomers(Pageable pageable);
    ApiResponseDto searchCustomers(String fullName, LocalDate startDate, LocalDate endDate, int page, int size);
    ApiResponseDto updateCustomer(String customerId, CustomerInputDto dto);
    ApiResponseDto deleteCustomer(String customerId);
}

