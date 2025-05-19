package com.dtbgroup.customer_service.controller;

import com.dtbgroup.customer_service.dto.ApiResponseDto;
import com.dtbgroup.customer_service.dto.CustomerInputDto;
import com.dtbgroup.customer_service.dto.response.CustomerResponseDto;
import com.dtbgroup.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> createCustomer(@RequestBody CustomerInputDto dto) {
        return ResponseEntity.ok(customerService.createCustomer(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> getCustomerById(@PathVariable String id,    @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(customerService.getCustomerById(id,pageable));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllCustomers(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }


    @GetMapping("/search-customer")
    public ResponseEntity<ApiResponseDto> searchCustomer(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationEndDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
      return ResponseEntity.ok(customerService.searchCustomers(fullName,creationStartDate,creationEndDate,page,size));

    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateCustomer(@PathVariable String id, @RequestBody CustomerInputDto dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteCustomer(@PathVariable String id) {
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}

