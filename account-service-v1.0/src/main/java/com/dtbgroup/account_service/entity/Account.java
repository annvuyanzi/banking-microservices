package com.dtbgroup.account_service.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_accounts")
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String accountId;
    @NotBlank(message = "IBAN is mandatory")
    @Column(name = "iban", nullable = false, unique = true)
    private String iban;
    @NotBlank(message = "BIC Swift is mandatory")
    @Column(name = "bic_swift", nullable = false)
    private String bicSwift;
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    @Column(name = "card_alias")
    private String cardAlias;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updatedAt", nullable = false, updatable = false)
    private LocalDateTime updatedAt;
    @Column(name = "createdBy", nullable = false, updatable = false)
    private LocalDateTime createdBy;
    private String status = "NEW";
    private boolean isEnabled;
}


