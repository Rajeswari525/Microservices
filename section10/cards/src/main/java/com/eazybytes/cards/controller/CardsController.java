package com.eazybytes.cards.controller;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsContactInfoDto;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.dto.ResponseDto;
import com.eazybytes.cards.entities.Cards;
import com.eazybytes.cards.service.ICardsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@Validated
public class CardsController {
    private ICardsService cardsService;

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

    public CardsController(ICardsService cardsService) {

        this.cardsService = cardsService;
    }


    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

    @PostMapping(path = "/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam
                                                  @Pattern(regexp = "^[0-9]{10}$", message = "Please enter valid mobile number")
                                                  String mobileNumber){
        cardsService.createCard(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(CardsConstants.STATUS_201))
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCard(@RequestHeader("eazyBank-correlation-id") String correlationId, @RequestParam
                                                  @Pattern(regexp = "^[0-9]{10}$", message = "Please enter valid mobile number")
                                                  String mobileNumber){
        logger.debug("eazyBank-correlation-id found in CardsController : {}", correlationId);
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


    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildVersion);
    }

    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }
}
