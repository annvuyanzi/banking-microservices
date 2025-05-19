package com.dtbgroup.card_service.service;

import com.dtbgroup.card_service.dto.response.ApiResponseDto;
import com.dtbgroup.card_service.dto.request.CardInputDto;
import org.springframework.data.domain.Pageable;

public interface CardService {
    ApiResponseDto createCard(CardInputDto dto);
    ApiResponseDto getCardById(String id,boolean isMasked,Pageable pageable);
    ApiResponseDto searchCards(String alias, String type, String pan, boolean isMasked, Pageable pageable);
    ApiResponseDto getAllCards(boolean isMasked, Pageable pageable);
    ApiResponseDto updateCard(String id, CardInputDto dto);
    ApiResponseDto deleteCard(String id);

    boolean existsAccountById(String accountId);
}
