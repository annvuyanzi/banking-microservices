package com.dtbgroup.account_service.controller;

import com.dtbgroup.account_service.dto.response.AccountResponseDto;
import com.dtbgroup.account_service.dto.response.ApiResponseDto;
import com.dtbgroup.account_service.entity.Account;
import com.dtbgroup.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ApiResponseDto getAllAccounts(  @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return accountService.getAllAccounts(page,size);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseDto> getAccountById(@PathVariable String id,   @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(accountService.getAccountById(id,page,size));
    }

    @PostMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseDto> createAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDto> updateAccount(@PathVariable String id, @RequestBody Account account) {
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponseDto> deleteAccount(@PathVariable String id) {

        return ResponseEntity.ok(accountService.deleteAccount(id));
    }
    @GetMapping("/search-account")
    public ResponseEntity<ApiResponseDto> searchAccount(
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) String bicSwift,
            @RequestParam(required = false) String cardAlias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(accountService.searchAccounts(iban, bicSwift, cardAlias, page, size));
    }

}
