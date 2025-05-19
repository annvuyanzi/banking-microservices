package com.dtbgroup.account_service.service.impl;

import com.dtbgroup.account_service.config.ConfigProperties;
import com.dtbgroup.account_service.dto.response.AccountResponseDto;
import com.dtbgroup.account_service.dto.response.ApiResponseDto;
import com.dtbgroup.account_service.dto.response.CustomerApiResponseDto;
import com.dtbgroup.account_service.dto.response.CustomerResponseDto;
import com.dtbgroup.account_service.entity.Account;
import com.dtbgroup.account_service.repository.AccountRepository;
import com.dtbgroup.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
   @Autowired
    private  ConfigProperties configProperties;
    @Autowired
    private  WebClient webClient;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public ApiResponseDto getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accountPage = accountRepository.findAll(pageable);
        List<AccountResponseDto> accountResponseDtoList=new ArrayList<>();
        List<AccountResponseDto> accountResponse = accountPage.getContent().stream()
                .map(                    account -> {
                    AccountResponseDto dto = new AccountResponseDto();
                    dto.setAccountId(account.getAccountId());
                    dto.setIban(account.getIban());
                    dto.setBicSwift(account.getBicSwift());
                    dto.setCustomerId(account.getCustomerId());
                    dto.setStatus(account.getStatus());
                    dto.setEnabled(account.isEnabled());
                    return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("content", accountResponse);
        paginatedData.put("currentPage", accountPage.getNumber());
        paginatedData.put("totalItems", accountPage.getTotalElements());
        paginatedData.put("totalPages", accountPage.getTotalPages());
        return new ApiResponseDto("0","success","200","Account Retrieved Successfully",null,paginatedData);

    }
    @Override
    public ApiResponseDto getAccountById(String id,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
      Page<Account> accountPage = accountRepository.findByAccountId(id, pageable);

        List<AccountResponseDto> accountResponse = accountPage.getContent().stream()
                .map(                    account -> {
                        AccountResponseDto dto = new AccountResponseDto();
                        dto.setAccountId(account.getAccountId());
                        dto.setIban(account.getIban());
                        dto.setBicSwift(account.getBicSwift());
                        dto.setCustomerId(account.getCustomerId());
                        dto.setStatus(account.getStatus());
                        dto.setEnabled(account.isEnabled());
                        return dto;
                })
                .collect(Collectors.toList());

        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("content", accountResponse);
        paginatedData.put("currentPage", accountPage.getNumber());
        paginatedData.put("totalItems", accountPage.getTotalElements());
        paginatedData.put("totalPages", accountPage.getTotalPages());
        return new ApiResponseDto("0","success","200","Account Retrieved Successfully",null,paginatedData);

    }
    @Override
    public ApiResponseDto createAccount(Account account) {


        //check if customer exists using //account.getCustomerId()
       if(!existsCustomerById(account.getCustomerId())){
           return new ApiResponseDto("1","failed","400","Customer Does not Exist,create customer first!",null,null);

       }
        //
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        Account accCreated = accountRepository.save(account);
        AccountResponseDto dto=new AccountResponseDto();
        dto.setAccountId(accCreated.getAccountId());
        dto.setIban(accCreated.getIban());
        dto.setStatus(accCreated.getStatus());
        dto.setBicSwift(accCreated.getBicSwift());
        dto.setEnabled(accCreated.isEnabled());
        dto.setCustomerId(accCreated.getCustomerId());
       return new ApiResponseDto("0","success","200","Account Created Successfully",null,dto);
    }
    @Override
    public ApiResponseDto updateAccount(String id, Account updatedAccount) {
        return accountRepository.findById(id).map(account -> {
            account.setIban(updatedAccount.getIban());
            account.setBicSwift(updatedAccount.getBicSwift());
            account.setCustomerId(updatedAccount.getCustomerId());
            account.setUpdatedAt(LocalDateTime.now());
            account.setStatus(updatedAccount.getStatus());
            account.setEnabled(updatedAccount.isEnabled());
            Account accUpdated = accountRepository.save(account);
            AccountResponseDto dto=new AccountResponseDto();
            dto.setAccountId(accUpdated.getAccountId());
            dto.setIban(accUpdated.getIban());
            dto.setStatus(accUpdated.getStatus());
            dto.setBicSwift(accUpdated.getBicSwift());
            dto.setEnabled(accUpdated.isEnabled());
            dto.setCustomerId(account.getCustomerId());
            return new ApiResponseDto("0","success","200","Account Created Successfully",null,dto);

        }).orElseThrow(() -> new RuntimeException("Account not found"));
    }
    @Override
    public ApiResponseDto deleteAccount(String id) {
         accountRepository.deleteById(id);
        return new ApiResponseDto("0","success","200","Account Deleted Successfully",null,null);
    }
    @Override
    public ApiResponseDto searchAccounts(String iban, String bicSwift, String cardAlias, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountRepository.searchByFilters(iban, bicSwift, cardAlias, pageable);
        Page<AccountResponseDto> paginatedList = accounts.map(account -> {
            AccountResponseDto dto = new AccountResponseDto();
            dto.setAccountId(account.getAccountId());
            dto.setIban(account.getIban());
            dto.setBicSwift(account.getBicSwift());
            dto.setCustomerId(account.getCustomerId());
            dto.setStatus(account.getStatus());
            dto.setEnabled(account.isEnabled());
            return dto;
        });
        return new ApiResponseDto("0","success","200","Account retrieved Successfully",null,paginatedList);

    }

    public boolean existsCustomerById(String customerId) {
        String url = configProperties.getCustomer().getBaseUrl()+"/" + customerId;
        String custId = "";
        CustomerApiResponseDto apiRes = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(CustomerApiResponseDto.class).block().getBody();
        if(apiRes.getStatusCode().equalsIgnoreCase("0")){
            //get customerId
            int totalItems = apiRes.getPrimaryData().getTotalItems();
            if(totalItems>0){
                return true;
            }else return false;
        }else {
            return  false;
        }
    }


}

