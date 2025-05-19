package com.dtbgroup.customer_service.repository;

import com.dtbgroup.customer_service.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT c FROM Customer c WHERE " +
            "(:fullName IS NULL OR CONCAT(c.firstName, ' ', c.lastName, ' ', c.otherName) LIKE %:fullName%) AND " +
            "(:startDate IS NULL OR c.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR c.createdAt <= :endDate)")
    Page<Customer> searchCustomers(@Param("fullName") String fullName,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   Pageable pageable);
    Page<Customer> findByCustomerId(String customerId,Pageable pageable);

}
