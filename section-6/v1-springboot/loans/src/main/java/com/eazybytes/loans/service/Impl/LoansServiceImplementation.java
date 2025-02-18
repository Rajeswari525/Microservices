package com.eazybytes.loans.service.Impl;

import com.eazybytes.loans.constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entities.Loans;
import com.eazybytes.loans.exception.LoanAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class LoansServiceImplementation implements ILoansService {

    @Autowired
    private LoansRepository loansRepository;

    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> existingLoan = loansRepository.findByMobileNumber(mobileNumber);
        if(existingLoan.isPresent()){
            throw new LoanAlreadyExistsException("Loan already exists with mobile number "+mobileNumber);
        }
        else{
            log.info("Loan created started ");
            Loans loan = new Loans();
            loan.setMobileNumber(mobileNumber);
            long loanNumber = 1000000000L+new Random().nextInt(100000000);
            loan.setLoanNumber(Long.toString(loanNumber));
            loan.setLoanType(LoansConstants.HOME_LOAN);
            loan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
            loan.setAmountPaid(0);
            loan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
            log.info("Loan created is "+loan);
            loansRepository.save(loan);
        }
    }

    @Override
    public LoansDto fetchLoanByMobileNumber(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Loan not found with mobile number "+mobileNumber));
        LoansDto loansDto = LoansMapper.mapLoansToLoansDto(loan, new LoansDto());
        return loansDto;
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        boolean isUpdated = false;
        if(!isUpdated){
            Loans loan = loansRepository.findByMobileNumber(loansDto.getMobileNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("Loan not found with mobile number "+loansDto.getMobileNumber()));
            LoansMapper.mapLoansDtoToLoans(loansDto, loan);
            loansRepository.save(loan);
           isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteLoanByMobileNumber(String mobileNumber) {
        boolean isUpdated = false;
        if(!isUpdated){
            Loans loan = loansRepository.findByMobileNumber(mobileNumber)
                    .orElseThrow(()-> new ResourceNotFoundException("Loan not found with mobile number "+mobileNumber));
            loansRepository.deleteById(loan.getLoanId());
            isUpdated = true;
        }
        return isUpdated;
    }
}
