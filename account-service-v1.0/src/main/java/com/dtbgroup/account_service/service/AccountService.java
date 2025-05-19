package com.dtbgroup.account_service.service;

import com.dtbgroup.account_service.dto.response.AccountResponseDto;
import com.dtbgroup.account_service.dto.response.ApiResponseDto;
import com.dtbgroup.account_service.entity.Account;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    ApiResponseDto getAllAccounts(int page, int size);

    ApiResponseDto getAccountById(String id,int page, int size);

    ApiResponseDto createAccount(Account account);

    ApiResponseDto updateAccount(String id, Account updatedAccount);

    ApiResponseDto deleteAccount(String id);

    ApiResponseDto searchAccounts(String iban, String bicSwift, String cardAlias, int page, int size);
}
