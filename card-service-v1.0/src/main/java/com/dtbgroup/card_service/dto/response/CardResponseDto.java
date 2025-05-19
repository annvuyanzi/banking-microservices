package com.dtbgroup.card_service.dto.response;

import lombok.Data;

@Data
public class CardResponseDto {
    private String cardId;
    private String cardAlias;
    private String accountId;
    private String  typeOfCard;// virtual or physical
    private String pan;
    private String cvv;
    private  String status;
    private boolean isEnabled ;
}