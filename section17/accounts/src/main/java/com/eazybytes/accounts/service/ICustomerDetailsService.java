package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDetailsDto;

public interface ICustomerDetailsService {
    CustomerDetailsDto fetchCustomerDetails(String correlationId, String mobileNumber);
}
