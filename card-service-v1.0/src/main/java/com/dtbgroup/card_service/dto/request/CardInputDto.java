package com.dtbgroup.card_service.dto.request;

import com.dtbgroup.card_service.dto.CardType;
import lombok.Data;

@Data
public class CardInputDto {
    private String cardAlias;
    private String accountId;
    private CardType typeOfCard;// virtual or physical
    private  String status;
}
