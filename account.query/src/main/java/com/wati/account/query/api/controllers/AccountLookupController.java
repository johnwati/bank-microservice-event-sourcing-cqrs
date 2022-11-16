package com.wati.account.query.api.controllers;


import com.wati.account.query.api.dto.AccountBalanceLookupResponse;
import com.wati.account.query.api.dto.AccountLookupResponse;
import com.wati.account.query.api.dto.EqualityType;
import com.wati.account.query.api.queries.*;
import com.wati.account.query.domain.entity.BankAccount;
import com.wati.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path ="/api/v1/bankAccountLookup")
public class AccountLookupController {

    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    private final QueryDispatcher queryDispatcher;

    @Autowired
    public AccountLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccount(){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank accounts(s)",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get all accounts request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(
            @PathVariable(value = "id") String id){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountsByIdQuery(id));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned bank account",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts by ID request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/getAccountByAccountNumber/{accountNumber}")
    public ResponseEntity<AccountLookupResponse> getByAccountNumber(
            @PathVariable(value = "accountNumber") Integer accountNumber){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByAccountNumber(accountNumber));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(new AccountLookupResponse("Account Number Not found"),HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned bank account",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts by ID request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/getAccountBalance/{accountNumber}")
    public ResponseEntity<AccountBalanceLookupResponse> getAccountBalance(
            @PathVariable(value = "accountNumber") Integer accountNumber){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByAccountNumber(accountNumber));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            var response = AccountBalanceLookupResponse.builder()
                    .balance(accounts.get(0).getBalance())
                    .message(MessageFormat.format("Successfully returned  account balance",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get account Balance";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountBalanceLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(
            @PathVariable(value = "accountHolder") String accountHolder){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned bank account",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts by Holder request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountWithBalance(
            @PathVariable(value = "equalityType") EqualityType equalityType,
            @PathVariable(value = "balance")  double balance){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType,balance));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            var response = AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message(MessageFormat.format("Successfully returned {0} bank accounts(s)",accounts.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts with balance request!";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new AccountLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
