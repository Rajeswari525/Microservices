package com.eazybytes.loans.controller;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansContactInfoDto;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.dto.ResponseDto;
import com.eazybytes.loans.service.ILoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
public class LoansController {

    private final ILoansService loansService;

    private static final Logger logger = LoggerFactory.getLogger(LoansController.class);

    public LoansController(ILoansService loansService) {
        this.loansService = loansService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansContactInfoDto loansContactInfoDto;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                      @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
                                                      String mobileNumber) {
        loansService.createLoan(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_201))
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(@RequestHeader("eazybank-correlation-id") String correlationId,
                                              @RequestParam @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
                                                  String mobileNumber){
        logger.debug("eazyBank-correlation-id found in LoansController : {}", correlationId);
        LoansDto loansDto = loansService.fetchLoanByMobileNumber(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_200))
                .body(loansDto);

    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoan(@RequestBody @Valid LoansDto loansDto){
        boolean status =  loansService.updateLoan(loansDto);
        if(status){
            return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_200))
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }
        else{
            return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_417))
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoan(@RequestParam @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
                                                      String mobileNumber){
        boolean status =  loansService.deleteLoanByMobileNumber(mobileNumber);
        if(status){
            return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_200))
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }
        else{
            return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_417))
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
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
    public ResponseEntity<LoansContactInfoDto> getContactInfo(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(loansContactInfoDto);
    }
}
