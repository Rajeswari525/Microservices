package com.eazybytes.cards.repository;

import com.eazybytes.cards.entities.Cards;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {
    public Optional<Cards> findByMobileNumber(String mobileNumber);

    @Transactional
    @Modifying
    public void deleteByMobileNumber(String mobileNumber);
}
