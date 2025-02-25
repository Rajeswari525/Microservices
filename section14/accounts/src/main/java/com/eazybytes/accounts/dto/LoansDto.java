package com.eazybytes.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class LoansDto {

    @NotEmpty(message = "Mobile Number cannot be null or empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid mobile number")
    private String mobileNumber;

    @NotEmpty(message = "Loan Number cannot be null or empty")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid Loan number")
    private String loanNumber;

    @NotEmpty(message = "Loan Type cannot be null or empty")
    private String loanType;

    @Positive(message = "Total Loan cannot be negative")
    private int totalLoan;

    @PositiveOrZero(message = "Amount Paid cannot be negative")
    private int amountPaid;

    @PositiveOrZero(message = "Outstanding Amount cannot be negative")
    private int outstandingAmount;
}
