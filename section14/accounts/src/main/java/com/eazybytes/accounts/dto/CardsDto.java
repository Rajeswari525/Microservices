package com.eazybytes.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CardsDto {

    @NotEmpty(message="Please enter valid mobile number")
    private String mobileNumber;

    @NotEmpty(message="Please enter valid card number")
    private String cardNumber;

    @NotEmpty(message="Please enter valid card type")
    private String cardType;

    @PositiveOrZero(message="Total limit cannot be negative")
    private int totalLimit;

    @PositiveOrZero(message="Amount used cannot be negative")
    private int amountUsed;

    @PositiveOrZero(message="Available amount cannot be negative")
    private int availableAmount;
}
