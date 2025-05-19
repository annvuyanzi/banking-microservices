package com.dtbgroup.card_service.entity;

import com.dtbgroup.card_service.dto.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_cards")

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;
    @Column(name = "card_alias")
    private String cardAlias;

    @NotNull(message = "Account ID is mandatory")
    @Column(name = "account_id", nullable = false)
    private String accountId;

    @NotNull(message = "Card type is mandatory")
    @Enumerated(EnumType.STRING)
    private CardType typeOfCard;// virtual or physical

    @Column(name = "identifier_one", nullable = false, unique = true)
    @NotBlank(message = "PAN is mandatory")
    @Pattern(regexp = "^\\d{16}$", message = "PAN must be 16 digits")
    private String pan;

    @Column(name = "identifier_two", nullable = false, unique = true)
    @NotBlank(message = "CVV is mandatory")
    @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits")
    private String cvv;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime createdBy;
    private String status;
    private boolean isEnabled;

    public String getMaskedPan() {
        if (pan == null || pan.length() != 16) {
            return pan;
        }
        return pan.substring(0, 6) + "******" + pan.substring(12);
    }

    public String getMaskedCvv() {
        return "***";
    }
}
