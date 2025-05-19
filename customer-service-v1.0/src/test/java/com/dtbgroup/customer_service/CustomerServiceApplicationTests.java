package com.dtbgroup.customer_service;

import com.dtbgroup.customer_service.dto.ApiResponseDto;
import com.dtbgroup.customer_service.dto.CustomerInputDto;
import com.dtbgroup.customer_service.entity.Customer;
import com.dtbgroup.customer_service.repository.CustomerRepository;
import com.dtbgroup.customer_service.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.yml")
class CustomerServiceApplicationTests {

		@Autowired
		private CustomerServiceImpl customerService;

		@MockBean
		private CustomerRepository customerRepository;

		private Customer customer;
		private CustomerInputDto inputDto;
		@Test
		void contextLoads() {
		}
		@BeforeEach
		void setUp() {
			customer = new Customer();
			customer.setCustomerId("123");
			customer.setFirstName("John");
			customer.setLastName("Doe");
			customer.setOtherName("Smith");
			customer.setStatus("ACTIVE");
			customer.setEnabled(true);

			inputDto = new CustomerInputDto();
			inputDto.setFirstName("John");
			inputDto.setLastName("Doe");
			inputDto.setOtherName("Smith");
			inputDto.setStatus("ACTIVE");
		}

		@Test
		void testCreateCustomer() {
			when(customerRepository.save(any(Customer.class))).thenReturn(customer);

			ApiResponseDto response = customerService.createCustomer(inputDto);

			assertEquals("Customer created successfully", response.getMessageDescription());
			verify(customerRepository, times(1)).save(any(Customer.class));
		}

		@Test
		void testGetCustomerById() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Customer> page = new PageImpl<>(List.of(customer));

			when(customerRepository.findByCustomerId("123", pageable)).thenReturn(page);

			ApiResponseDto response = customerService.getCustomerById("123", pageable);

			assertEquals("Customer retrieved successfully", response.getMessageDescription());
			verify(customerRepository).findByCustomerId("123", pageable);
		}

		@Test
		void testGetAllCustomers() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Customer> page = new PageImpl<>(List.of(customer));

			when(customerRepository.findAll(pageable)).thenReturn(page);

			ApiResponseDto response = customerService.getAllCustomers(pageable);

			assertEquals("All customers retrieved successfully", response.getMessageDescription());
			verify(customerRepository).findAll(pageable);
		}

		@Test
		void testUpdateCustomer_Success() {
			when(customerRepository.findById("123")).thenReturn(Optional.of(customer));
			when(customerRepository.save(any(Customer.class))).thenReturn(customer);

			ApiResponseDto response = customerService.updateCustomer("123", inputDto);

			assertEquals("Customer updated successfully", response.getMessageDescription());
			verify(customerRepository).save(any(Customer.class));
		}

		@Test
		void testUpdateCustomer_NotFound() {
			when(customerRepository.findById("123")).thenReturn(Optional.empty());

			ApiResponseDto response = customerService.updateCustomer("123", inputDto);

			assertEquals("Customer not found", response.getMessageDescription());
		}

		@Test
		void testDeleteCustomer_Success() {
			when(customerRepository.findById("123")).thenReturn(Optional.of(customer));

			ApiResponseDto response = customerService.deleteCustomer("123");

			assertEquals("Customer deleted successfully", response.getMessageDescription());
			verify(customerRepository).delete(customer);
		}

		@Test
		void testDeleteCustomer_NotFound() {
			when(customerRepository.findById("123")).thenReturn(Optional.empty());

			ApiResponseDto response = customerService.deleteCustomer("123");

			assertEquals("Customer not found", response.getMessageDescription());
		}


}
