package com.dtbgroup.customer_service.service.impl;

import com.dtbgroup.customer_service.dto.ApiResponseDto;
import com.dtbgroup.customer_service.dto.CustomerInputDto;
import com.dtbgroup.customer_service.dto.response.CustomerResponseDto;
import com.dtbgroup.customer_service.entity.Customer;
import com.dtbgroup.customer_service.repository.CustomerRepository;
import com.dtbgroup.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public ApiResponseDto createCustomer(CustomerInputDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setOtherName(dto.getOtherName());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setStatus("ACTIVE");
        customer.setEnabled(true);
        Customer custRec = customerRepository.save(customer);

        return buildSuccessResponse(toResponseDto(custRec), "Customer created successfully");
    }

    @Override
    public ApiResponseDto getCustomerById(String id, Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findByCustomerId(id, pageable);

        List<CustomerResponseDto> content = customerPage.getContent()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

                    Map<String, Object> paginatedData = new HashMap<>();
                    paginatedData.put("content", content);
                    paginatedData.put("currentPage", customerPage.getNumber());
                    paginatedData.put("totalItems", customerPage.getTotalElements());
                    paginatedData.put("totalPages", customerPage.getTotalPages());

                    return buildSuccessResponse(paginatedData, "Customer retrieved successfully");


    }

    @Override
    public ApiResponseDto getAllCustomers(Pageable pageable) {
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerResponseDto> content = customerPage.getContent()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("content", content);
        paginatedData.put("currentPage", customerPage.getNumber());
        paginatedData.put("totalItems", customerPage.getTotalElements());
        paginatedData.put("totalPages", customerPage.getTotalPages());

        return buildSuccessResponse(paginatedData, "All customers retrieved successfully");
    }

    @Override
    public ApiResponseDto searchCustomers(String fullName, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerResponseDto> customerDetails = customerRepository.searchCustomers(fullName, startDate, endDate, pageable)
                .map(this::mapToDto);
        return buildSuccessResponse(customerDetails,"Customer Details retrieved");
    }

    @Override
    public ApiResponseDto updateCustomer(String id, CustomerInputDto dto) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(dto.getFirstName());
                    customer.setLastName(dto.getLastName());
                    customer.setOtherName(dto.getOtherName());
                    customer.setStatus(dto.getStatus());
                    customer.setUpdatedAt(LocalDateTime.now());
                    customerRepository.save(customer);
                    return buildSuccessResponse(toResponseDto(customer), "Customer updated successfully");
                })
                .orElseGet(() -> buildErrorResponse("404", "Customer not found"));
    }

    @Override
    public ApiResponseDto deleteCustomer(String id) {
        return customerRepository.findById(id)
                .map(customer -> {
                    customerRepository.delete(customer);
                    return buildSuccessResponse(null, "Customer deleted successfully");
                })
                .orElseGet(() -> buildErrorResponse("404", "Customer not found"));
    }


    private CustomerResponseDto mapToDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setOtherName(customer.getOtherName());
        dto.setStatus(customer.getStatus());
        dto.setEnabled(customer.isEnabled());
        return dto;
    }

    private CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setOtherName(customer.getOtherName());
        dto.setStatus(customer.getStatus());
        dto.setEnabled(customer.isEnabled());
        return dto;
    }

    private ApiResponseDto buildSuccessResponse(Object data, String message) {
        return ApiResponseDto.builder()
                .statusCode("0")
                .statusDescription("SUCCESS")
                .messageCode("200")
                .messageDescription(message)
                .primaryData(data)
                .build();
    }

    private ApiResponseDto buildErrorResponse(String code, String message) {
        return ApiResponseDto.builder()
                .statusCode("1")
                .statusDescription("ERROR")
                .messageCode(code)
                .messageDescription(message)
                .build();
    }
}

