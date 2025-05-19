package com.dtbgroup.card_service.controller;

import com.dtbgroup.card_service.dto.response.ApiResponseDto;
import com.dtbgroup.card_service.dto.request.CardInputDto;
import com.dtbgroup.card_service.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> createCard(@RequestBody CardInputDto dto) {
        return ResponseEntity.ok(cardService.createCard(dto));
    }

    @GetMapping("/{id}/{isMasked}")
    public ResponseEntity<ApiResponseDto> getCardById(@PathVariable String id,@PathVariable boolean isMasked,   @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cardService.getCardById(id,isMasked,pageable));
    }

    @GetMapping("search-card")
    public ResponseEntity<ApiResponseDto> searchCards(
            @RequestParam(required = false) String alias,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String pan,
            @RequestParam(defaultValue = "false") boolean isMasked,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cardService.searchCards(alias, type, pan, isMasked, pageable));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllCards(boolean isMasked, @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cardService.getAllCards(isMasked,pageable));
    }



    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateCard(@PathVariable String id, @RequestBody CardInputDto dto) {
        return ResponseEntity.ok(cardService.updateCard(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable String id) {
        return ResponseEntity.ok(cardService.deleteCard(id));
    }
}

