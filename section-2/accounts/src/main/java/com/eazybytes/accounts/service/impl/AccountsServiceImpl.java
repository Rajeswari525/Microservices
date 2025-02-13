package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entities.Accounts;
import com.eazybytes.accounts.entities.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountsMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> customerOptional = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(customerOptional.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber \"\n"+customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Raji");
        customerRepository.save(customer);

        createAccountFromCustomer(customer);

    }

    public Accounts createAccountFromCustomer(Customer customer){
        Accounts account = new Accounts();
        account.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        account.setAccountNumber(randomAccNumber);
        account.setAccountType(AccountsConstants.SAVINGS);
        account.setBranchAddress(AccountsConstants.ADDRESS);
        account.setCreatedAt(LocalDateTime.now());
        account.setCreatedBy("Raji");
        return accountsRepository.save(account);
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer","mobileNumber", mobileNumber));
        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Account","mobileNumber", mobileNumber));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        if(!isUpdated){
            AccountsDto accountsDto = customerDto.getAccountsDto();
            Accounts account = accountsRepository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(()-> new ResourceNotFoundException("Accounts","AccountNumber", accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, account);
            account = accountsRepository.save(account);
            Long customerId = account.getCustomerId();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(()-> new ResourceNotFoundException("Customer","CustomerId", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customer = customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Customer","MobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }


}
