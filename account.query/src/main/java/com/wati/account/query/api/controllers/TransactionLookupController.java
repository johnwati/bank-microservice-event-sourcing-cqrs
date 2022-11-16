package com.wati.account.query.api.controllers;

import com.wati.account.query.api.dto.AccountLookupResponse;
import com.wati.account.query.api.dto.OverdraftLookupResponse;
import com.wati.account.query.api.dto.TransactionLookupResponse;
import com.wati.account.query.api.queries.FindAccountByAccountNumber;
import com.wati.account.query.api.queries.FindAccountsByIdQuery;
import com.wati.account.query.api.queries.FindTransactionByAccountId;
import com.wati.account.query.domain.entity.AccountTransaction;
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
@RequestMapping(path ="/api/v1/transactionLookup")
public class TransactionLookupController  {
    private final Logger logger = Logger.getLogger(TransactionLookupController.class.getName());

    private final QueryDispatcher queryDispatcher;

    @Autowired
    public TransactionLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }



    @GetMapping(path = "/findByAccountId/{id}")
    public ResponseEntity<TransactionLookupResponse> findTransactionsByAccountId(@PathVariable(value = "id") String id){
        try{

            List<AccountTransaction> transactionList = queryDispatcher.send(new FindTransactionByAccountId(id));
            if(transactionList ==null || transactionList.size() == 0){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = TransactionLookupResponse.builder()
                    .transactions(transactionList)
                    .message(MessageFormat.format("Successfully returned account transaction(s) {0}",transactionList.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts transactions by ID request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new TransactionLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findByAccountNumber/{accountNumber}")
    public ResponseEntity<TransactionLookupResponse> findTransactionsByAccountNumber(@PathVariable(value = "accountNumber") Integer accountNumber){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByAccountNumber(accountNumber));
            System.out.println("======"+accounts.toString());
            if(accounts.size() <= 0){
                var safeErrorMessage = "Account Number Not Found";
                return new ResponseEntity<>(new TransactionLookupResponse(safeErrorMessage), HttpStatus.NOT_FOUND);
            }
            List<AccountTransaction> transactionList = queryDispatcher.send(new FindTransactionByAccountId(accounts.get(0).getId()));
            if(transactionList ==null || transactionList.size() == 0){
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            var response = TransactionLookupResponse.builder()
                    .transactions(transactionList)
                    .message(MessageFormat.format("Successfully returned bank account Transactions(s) {0}",transactionList.size()))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get accounts by AccountNumber request";
            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new TransactionLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/checkOverdraftLimit/{accountNumber}/{debitAmount}")
    public ResponseEntity<OverdraftLookupResponse> checkOverdraftLimit(
            @PathVariable(value = "accountNumber") Integer accountNumber,
            @PathVariable(value = "debitAmount") Integer debitAmount){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByAccountNumber(accountNumber));
            if(accounts ==null || accounts.size() == 0){
                return new ResponseEntity<>(new OverdraftLookupResponse("Account Not Found"),HttpStatus.NO_CONTENT);
            }
            var response = OverdraftLookupResponse.builder()
                    .overdraftLimitExceeded(debitAmount > accounts.get(0).getOverdraftLimit())
                    .message(MessageFormat.format("Successfully returned Overdraft Limit Check","d"))
                    .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            var safeErrorMessage = "Failed to complete get overdraft limit check request";

            logger.log(Level.SEVERE, safeErrorMessage,e);
            return new ResponseEntity<>(new OverdraftLookupResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
