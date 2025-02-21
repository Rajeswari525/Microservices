package com.eazybytes.cards.service.Impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entities.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.repository.CardsRepository;
import com.eazybytes.cards.service.ICardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class CardsServiceImpl implements ICardsService {

    @Autowired
    private CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> existingCard = cardsRepository.findByMobileNumber(mobileNumber);
        if(existingCard.isPresent()){
            throw new CardAlreadyExistsException("Card already exists with mobile number "+mobileNumber);
        }
        else{
            Cards newCard = new Cards();
            newCard.setMobileNumber(mobileNumber);
            long cardNumber = 1000000000L+new Random().nextLong(9000000000L);
            newCard.setCardNumber(String.valueOf(cardNumber));
            newCard.setCardType(CardsConstants.CREDIT_CARD);
            newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
            newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
            newCard.setAmountUsed(0);
            cardsRepository.save(newCard);
        }
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Card not found with mobile number "+mobileNumber));
        return CardsMapper.mapCardsToCardsDto(card,new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        String mobileNumber = cardsDto.getMobileNumber();
        Cards cardFound = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Card not found with mobile number "+mobileNumber));
        CardsMapper.mapCardsDtoToCards(cardsDto,cardFound);
        cardsRepository.save(cardFound);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cardFound = cardsRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Card not found with mobile number "+mobileNumber));
        log.info("Card found to delete is "+cardFound);
        cardsRepository.deleteByMobileNumber(cardFound.getMobileNumber());
        return true;
    }
}
