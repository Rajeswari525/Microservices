package com.eazybytes.cards.controller;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.dto.ResponseDto;
import com.eazybytes.cards.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@Validated
@AllArgsConstructor
public class CardsController {
    private ICardsService cardsService;

    @PostMapping(path = "/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam
                                                  @Pattern(regexp = "^[0-9]{10}$", message = "Please enter valid mobile number")
                                                  String mobileNumber){
        cardsService.createCard(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_201))
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestParam
                                                  @Pattern(regexp = "^[0-9]{10}$", message = "Please enter valid mobile number")
                                                  String mobileNumber){
        CardsDto cardsDto = cardsService.fetchCard(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_200))
                .body(cardsDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCard(@RequestBody @Valid CardsDto cardsDto){
        boolean status = cardsService.updateCard(cardsDto);
        if(status){
            return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_200))
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }
        else{
            return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_417))
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCard(@RequestParam
                                                  @Pattern(regexp = "^[0-9]{10}$", message = "Please enter valid mobile number")
                                                  String mobileNumber){
        boolean status = cardsService.deleteCard(mobileNumber);
        if(status){
            return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_200))
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }
        else{
            return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_417))
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }
}
