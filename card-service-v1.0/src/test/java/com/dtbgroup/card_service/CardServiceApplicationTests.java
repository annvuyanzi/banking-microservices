package com.dtbgroup.card_service;

import com.dtbgroup.card_service.config.ConfigProperties;
import com.dtbgroup.card_service.dto.CardType;
import com.dtbgroup.card_service.dto.request.CardInputDto;
import com.dtbgroup.card_service.dto.response.ApiResponseDto;
import com.dtbgroup.card_service.entity.Card;
import com.dtbgroup.card_service.repository.CardRepository;
import com.dtbgroup.card_service.service.impl.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(locations = "classpath:application-test.yml")
class CardServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	@MockBean
	private CardRepository cardRepository;

	@Autowired
	private ConfigProperties configProperties;
	@Autowired
	private CardServiceImpl cardService;


	private CardInputDto cardInputDto;
	private Card card;

	@BeforeEach
	void setUp() {

		cardInputDto = new CardInputDto();
		 cardInputDto.setAccountId("acc123");
		 cardInputDto.setCardAlias("My Card");
		cardInputDto.setTypeOfCard(CardType.PHYSICAL);
		 cardInputDto.setStatus("ACTIVE");

		card = new Card();
		card.setCardId("card123");
		card.setAccountId("acc123");
		card.setCardAlias("My Card");
		card.setTypeOfCard(CardType.PHYSICAL);
		card.setStatus("ACTIVE");
		card.setPan("1234567890123456");
		card.setCvv("123");
		card.setEnabled(true);
	}


	@Test
	void testCreateCard_Success() {
		Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(card);
		//Mockito.when(cardService.existsAccountById("acc123")).thenReturn(true);
		ApiResponseDto response = cardService.createCard(cardInputDto);

		assertEquals("1", response.getStatusCode());
		assertEquals("Account Does not Exist, Create Account First!", response.getMessageDescription());
	}

	@Test
	void testGetCardById_Success() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<Card> page = new PageImpl<>(List.of(card));
		Mockito.when(cardRepository.findByCardIdIgnoreCase("card123", pageable)).thenReturn(page);
		ApiResponseDto response = cardService.getCardById("card123", true, pageable);
		assertEquals("0", response.getStatusCode());
		assertEquals("Paginated card retrieved successfully", response.getMessageDescription());
	}

	@Test
	void testUpdateCard_Success() {
		Mockito.when(cardRepository.findById("card123")).thenReturn(Optional.of(card));
		Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenReturn(card);

		ApiResponseDto response = cardService.updateCard("card123", cardInputDto);

		assertEquals("0", response.getStatusCode());
		assertEquals("Card updated successfully", response.getMessageDescription());
	}

	@Test
	void testUpdateCard_NotFound() {
		Mockito.when(cardRepository.findById("card123")).thenReturn(Optional.empty());

		ApiResponseDto response = cardService.updateCard("card123", cardInputDto);

		assertEquals("1", response.getStatusCode());
		assertEquals("Card not found", response.getMessageDescription());
	}

	@Test
	void testDeleteCard_Success() {
		Mockito.when(cardRepository.findById("card123")).thenReturn(Optional.of(card));

		ApiResponseDto response = cardService.deleteCard("card123");

		assertEquals("0", response.getStatusCode());
		assertEquals("Card deleted successfully", response.getMessageDescription());
	}

	@Test
	void testDeleteCard_NotFound() {
		Mockito.when(cardRepository.findById("card123")).thenReturn(Optional.empty());

		ApiResponseDto response = cardService.deleteCard("card123");

		assertEquals("1", response.getStatusCode());
		assertEquals("Card not found", response.getMessageDescription());
	}

}
