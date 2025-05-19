package com.dtbgroup.account_service;

import com.dtbgroup.account_service.dto.response.ApiResponseDto;
import com.dtbgroup.account_service.entity.Account;
import com.dtbgroup.account_service.repository.AccountRepository;
import com.dtbgroup.account_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountServiceApplicationTests {


		@Test
		void contextLoads() {
		}

		@Spy
		@InjectMocks
		private AccountServiceImpl accountService;

		@Mock
		private AccountRepository accountRepository;

		private Account account;

		@BeforeEach
		void setUp() {
			account = new Account();
			account.setAccountId("123");
			account.setIban("DE89370400440532013000");
			account.setBicSwift("COBADEFFXXX");
			account.setCustomerId("456");
			account.setStatus("ACTIVE");
			account.setEnabled(true);
		}

		@Test
		void testGetAllAccounts() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Account> page = new PageImpl<>(List.of(account));

			when(accountRepository.findAll(pageable)).thenReturn(page);

			ApiResponseDto response = accountService.getAllAccounts(0, 10);

			assertEquals("Account Retrieved Successfully", response.getMessageDescription());
			verify(accountRepository).findAll(pageable);
		}

		@Test
		void testGetAccountById() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Account> page = new PageImpl<>(List.of(account));

			when(accountRepository.findByAccountId("123", pageable)).thenReturn(page);

			ApiResponseDto response = accountService.getAccountById("123", 0, 10);

			assertEquals("Account Retrieved Successfully", response.getMessageDescription());
			verify(accountRepository).findByAccountId("123", pageable);
		}

		@Test
		void testCreateAccount() {
			doReturn(true).when(accountService).existsCustomerById(account.getCustomerId());
			when(accountRepository.save(any(Account.class))).thenReturn(account);

			ApiResponseDto response = accountService.createAccount(account);

			assertEquals("Account Created Successfully", response.getMessageDescription());
			verify(accountRepository).save(any(Account.class));
		}

		@Test
		void testUpdateAccount_Success() {
			when(accountRepository.findById("123")).thenReturn(Optional.of(account));
			when(accountRepository.save(any(Account.class))).thenReturn(account);

			ApiResponseDto response = accountService.updateAccount("123", account);

			assertEquals("Account Created Successfully", response.getMessageDescription());
			verify(accountRepository).save(any(Account.class));
		}

		@Test
		void testUpdateAccount_NotFound() {
			when(accountRepository.findById("123")).thenReturn(Optional.empty());

			RuntimeException exception = assertThrows(RuntimeException.class, () -> {
				accountService.updateAccount("123", account);
			});

			assertEquals("Account not found", exception.getMessage());
		}

		@Test
		void testDeleteAccount() {
			doNothing().when(accountRepository).deleteById("123");

			ApiResponseDto response = accountService.deleteAccount("123");

			assertEquals("Account Deleted Successfully", response.getMessageDescription());
			verify(accountRepository).deleteById("123");
		}

		@Test
		void testSearchAccounts() {
			Pageable pageable = PageRequest.of(0, 10);
			Page<Account> page = new PageImpl<>(List.of(account));

			when(accountRepository.searchByFilters(anyString(), anyString(), anyString(), eq(pageable))).thenReturn(page);

			ApiResponseDto response = accountService.searchAccounts("DE89370400440532013000", "COBADEFFXXX", "alias", 0, 10);

			assertEquals("Account retrieved Successfully", response.getMessageDescription());
			verify(accountRepository).searchByFilters(anyString(), anyString(), anyString(), eq(pageable));
		}


}
