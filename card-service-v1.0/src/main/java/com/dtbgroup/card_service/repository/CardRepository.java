package com.dtbgroup.card_service.repository;

import com.dtbgroup.card_service.dto.CardType;
import com.dtbgroup.card_service.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    @Query("SELECT c FROM Card c WHERE " +
            "(:cardAlias IS NULL OR c.cardAlias LIKE %:cardAlias%) AND " +
            "(:cardType IS NULL OR c.typeOfCard = :cardType) AND " +
            "(:pan IS NULL OR c.pan LIKE %:pan%)")
    Page<Card> searchCards(
            @Param("cardAlias") String cardAlias,
            @Param("cardType") CardType typeOfCard,
            @Param("pan") String pan,
            Pageable pageable);

    long countByAccountIdAndTypeOfCard(String accountId, CardType typeOfCard);

    boolean existsByAccountIdAndTypeOfCard(String accountId, CardType typeOfCard);

    Page<Card> findByCardId(String cardId, Pageable pageable);
    Page<Card> findByCardIdIgnoreCase(String cardId, Pageable pageable);

}

