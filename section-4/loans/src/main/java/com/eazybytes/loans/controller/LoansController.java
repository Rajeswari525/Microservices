package com.eazybytes.loans.controller;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.dto.ResponseDto;
import com.eazybytes.loans.service.ILoansService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
@AllArgsConstructor
@Validated
public class LoansController {

    private ILoansService loansService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam
                                                      @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
                                                      String mobileNumber) {
        loansService.createLoan(mobileNumber);
        return ResponseEntity.status(Integer.parseInt(LoansConstants.STATUS_201))
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoan(@RequestParam @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
                                                  String mobileNumber){
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
}
