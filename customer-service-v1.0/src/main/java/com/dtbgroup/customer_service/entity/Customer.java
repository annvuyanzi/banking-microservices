package com.dtbgroup.customer_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String customerId;
    private String firstName;
    private String lastName;
    private String otherName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdBy;
    private  String status;
    private boolean isEnabled ;
}
