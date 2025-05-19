package com.dtbgroup.card_service.service.impl;

import com.dtbgroup.card_service.config.ConfigProperties;
import com.dtbgroup.card_service.dto.CardType;
import com.dtbgroup.card_service.dto.response.AccountApiResponseDto;
import com.dtbgroup.card_service.dto.response.ApiResponseDto;
import com.dtbgroup.card_service.dto.request.CardInputDto;
import com.dtbgroup.card_service.dto.response.CardResponseDto;
import com.dtbgroup.card_service.entity.Card;
import com.dtbgroup.card_service.repository.CardRepository;
import com.dtbgroup.card_service.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private static final int MAX_CARDS_PER_ACCOUNT = 2;
   @Autowired
    private ConfigProperties configProperties;
    private final WebClient webClient;
    @Override
    public ApiResponseDto createCard(CardInputDto dto) {

        if(!existsAccountById(dto.getAccountId())){
           return buildErrorResponse("1","Account Does not Exist, Create Account First!");
        }
        validateCardCreation(dto);

        Card card = new Card();
        card.setCardAlias(dto.getCardAlias());
        card.setAccountId(dto.getAccountId());
        card.setTypeOfCard(CardType.valueOf(String.valueOf(dto.getTypeOfCard())));
        card.setPan(generatePAN());
        card.setCvv(generateCVV());
        card.setStatus(dto.getStatus());
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        card.setEnabled(true);

        cardRepository.save(card);

        return buildSuccessResponse(toResponseDto(card,true), "Card created successfully");
    }

    @Override
    public ApiResponseDto getCardById(String id, boolean isMasked,Pageable pageable) {
        Page<Card> cardPage = cardRepository.findByCardIdIgnoreCase(id, pageable);

        List<CardResponseDto> cardDtos = cardPage.getContent().stream()
                .map(card -> toResponseDto(card, isMasked))
                .collect(Collectors.toList());

        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("content", cardDtos);
        paginatedData.put("currentPage", cardPage.getNumber());
        paginatedData.put("totalItems", cardPage.getTotalElements());
        paginatedData.put("totalPages", cardPage.getTotalPages());

        return ApiResponseDto.builder()
                .statusCode("0")
                .statusDescription("SUCCESS")
                .messageCode("200")
                .messageDescription("Paginated card retrieved successfully")
                .primaryData(paginatedData)
                .build();
    }

    @Override
    public ApiResponseDto searchCards(String alias, String type, String pan, boolean isMasked, Pageable pageable) {
    Page<Card> cardPage = cardRepository.searchCards(alias, CardType.valueOf(type), pan, pageable);

    List<CardResponseDto> cardDtos = cardPage.getContent().stream()
            .map(card -> toResponseDto(card, isMasked))
            .collect(Collectors.toList());

    Map<String, Object> paginatedData = new HashMap<>();
    paginatedData.put("content", cardDtos);
    paginatedData.put("currentPage", cardPage.getNumber());
    paginatedData.put("totalItems", cardPage.getTotalElements());
    paginatedData.put("totalPages", cardPage.getTotalPages());

    return ApiResponseDto.builder()
            .statusCode("0")
            .statusDescription("SUCCESS")
            .messageCode("200")
            .messageDescription("Filtered cards retrieved successfully")
            .primaryData(paginatedData)
            .build();
}

    @Override
    public ApiResponseDto getAllCards(boolean isMasked, Pageable pageable) {
        Page<Card> cardPage = cardRepository.findAll(pageable);

        // Map each Card to CardResponseDto with masking
        List<CardResponseDto> cardDtos = cardPage.getContent().stream()
                .map(card -> toResponseDto(card, isMasked))
                .collect(Collectors.toList());

        Map<String, Object> paginatedData = new HashMap<>();
        paginatedData.put("content", cardDtos);
        paginatedData.put("currentPage", cardPage.getNumber());
        paginatedData.put("totalItems", cardPage.getTotalElements());
        paginatedData.put("totalPages", cardPage.getTotalPages());

        return ApiResponseDto.builder()
                .statusCode("0")
                .statusDescription("SUCCESS")
                .messageCode("200")
                .messageDescription("All cards retrieved")
                .primaryData(paginatedData)
                .build();
    }

    @Override
    public ApiResponseDto updateCard(String id, CardInputDto dto) {
        return cardRepository.findById(id)
                .map(card -> {
                    card.setCardAlias(dto.getCardAlias());
                    card.setAccountId(dto.getAccountId());
                   // card.setTypeOfCard(CardType.valueOf(String.valueOf(card.getTypeOfCard())));
                    card.setStatus(dto.getStatus());
                    card.setUpdatedAt(LocalDateTime.now());

                    cardRepository.save(card);
                    return buildSuccessResponse(toResponseDto(card,true), "Card updated successfully");
                })
                .orElseGet(() -> buildErrorResponse("404", "Card not found"));
    }

    @Override
    public ApiResponseDto deleteCard(String id) {
        return cardRepository.findById(id)
                .map(card -> {
                    cardRepository.delete(card);
                    return buildSuccessResponse(null, "Card deleted successfully");
                })
                .orElseGet(() -> buildErrorResponse("404", "Card not found"));
    }

    private CardResponseDto toResponseDto(Card cardInput,boolean isMasked) {
       Card card =getMaskedDetails(cardInput,isMasked);
        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(card.getCardId());
        dto.setCardAlias(card.getCardAlias());
        dto.setAccountId(card.getAccountId());
        dto.setTypeOfCard(String.valueOf(card.getTypeOfCard()));
        dto.setPan(card.getPan());
        dto.setCvv(card.getCvv());
        dto.setStatus(card.getStatus());
        dto.setEnabled(card.isEnabled());
        return dto;
    }

    private void validateCardCreation(CardInputDto dto) {
        // Check if account already has a card of this type
        if (cardRepository.existsByAccountIdAndTypeOfCard(dto.getAccountId(), CardType.valueOf(String.valueOf(dto.getTypeOfCard())))) {
            throw new RuntimeException("Account already has a " + dto.getTypeOfCard() + " card");
        }

        // Check if account has reached maximum number of cards
        long totalCards = cardRepository.countByAccountIdAndTypeOfCard(dto.getAccountId(), CardType.VIRTUAL) +
                cardRepository.countByAccountIdAndTypeOfCard(dto.getAccountId(), CardType.PHYSICAL);

        if (totalCards >= MAX_CARDS_PER_ACCOUNT) {
            throw new RuntimeException("Account has reached maximum number of cards (" + MAX_CARDS_PER_ACCOUNT + ")");
        }
    }

    private ApiResponseDto buildSuccessResponse(Object data, String message) {
        return ApiResponseDto.builder()
                .statusCode("0")
                .statusDescription("SUCCESS")
                .messageCode("200")
                .messageDescription(message)
                .primaryData(data)
                .build();
    }

    public ApiResponseDto buildErrorResponse(String code, String message) {
        return ApiResponseDto.builder()
                .statusCode("1")
                .statusDescription("ERROR")
                .messageCode("400")
                .messageDescription(message)
                .build();
    }

    public Card getMaskedDetails(Card card, boolean shouldBeMasked ){

        if(shouldBeMasked){
           Card masked=maskCardDetails(card);
            return masked;
        }else {
            return card;
        }

    }

    private Card maskCardDetails(Card card) {
        String newPan = card.getPan().substring(0, 6) + "******" + card.getPan().substring(12);
        String newCvv="***";
        card.setPan(newPan);
        card.setCvv(newCvv);
        return card;
    }
    @Override
    public boolean existsAccountById(String accountId) {
        String url = configProperties.getAccount().getBaseUrl()+"/" + accountId;

        String custId = "";
        log.info("**** existsAccountById  URL: {}",url);
        AccountApiResponseDto apiRes = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(AccountApiResponseDto.class).block().getBody();
        log.info("**** response  StatusCode: {}",apiRes.getStatusCode());
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

    public String generatePAN() {
            String prefix = configProperties.getBin();
            int remainingDigits = 10;
            StringBuilder sb = new StringBuilder(prefix);
            Random random = new Random();

            for (int i = 0; i < remainingDigits; i++) {
                int digit = random.nextInt(10); // generates a digit from 0 to 9
                sb.append(digit);
            }
            return  sb.toString();

    }
    public String generateCVV() {

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10); // generates a digit from 0 to 9
            sb.append(digit);
        }

        String result = sb.toString();
        String lastThreeDigits = result.substring(result.length() - 3);
      return lastThreeDigits;
    }

}

