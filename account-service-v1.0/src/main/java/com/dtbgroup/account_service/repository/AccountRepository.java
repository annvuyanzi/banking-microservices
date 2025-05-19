package com.dtbgroup.account_service.repository;

import com.dtbgroup.account_service.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
    @Query("SELECT a FROM Account a WHERE " +
            "(:iban IS NULL OR a.iban = :iban) AND " +
            "(:bicSwift IS NULL OR a.bicSwift = :bicSwift) AND " +
            "(:cardAlias IS NULL OR a.cardAlias = :cardAlias)")
    Page<Account> searchByFilters(@Param("iban") String iban,
                                  @Param("bicSwift") String bicSwift,
                                  @Param("cardAlias") String cardAlias,
                                  Pageable pageable);
    Page<Account>findByAccountId(String accountId,Pageable pageable);
    Page<Account>findAll(Pageable pageable);

}
