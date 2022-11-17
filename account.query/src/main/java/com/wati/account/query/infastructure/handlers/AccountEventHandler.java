package com.wati.account.query.infastructure.handlers;

import com.wati.account.common.dto.TransactionStatus;
import com.wati.account.common.dto.TransactionType;
import com.wati.account.common.events.AccountCloseEvent;
import com.wati.account.common.events.AccountOpenedEvent;
import com.wati.account.common.events.CreditAccountEvent;
import com.wati.account.common.events.DebitAccountEvent;
import com.wati.account.query.domain.entity.AccountTransaction;
import com.wati.account.query.domain.repository.AccountRepository;
import com.wati.account.query.domain.entity.BankAccount;
import com.wati.account.query.domain.repository.AccountTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    private final AccountTransactionRepository accountTransactionRepository;

    @Autowired
    public AccountEventHandler(AccountRepository accountRepository, AccountTransactionRepository accountTransactionRepository) {
        this.accountRepository = accountRepository;
        this.accountTransactionRepository = accountTransactionRepository;
    }

    @Override
    public void on(AccountOpenedEvent event) {
        var bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .accountNumber(event.getAccountNumber())
                .externalAccountNumber(event.getExternalAccountNumber())
                .currency(event.getCurrency())
                .creationDate(event.getCreatedDate())
                .accountType(event.getAccountType())
                .openingBalance(event.getOpeningBalance())
                .balance(event.getOpeningBalance())
                .overdraftLimit(event.getOverdraftLimit())
                .build();
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(CreditAccountEvent event) {
        System.out.println("*********"+event);
        var bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            return;
        }
        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance + event.getAmount();
        bankAccount.get().setBalance(latestBalance);
        accountRepository.save(bankAccount.get());

        var accountTransaction = AccountTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .accountId(event.getId())
                .transactionType(TransactionType.CREDIT)
                .bookingAmount(event.getAmount())
                .externalReferenceNumber(event.getExternalTransactionNumber())
                .referenceNumber(UUID.randomUUID().toString().toUpperCase().replaceAll("-","").substring(5,18))
                .balance(latestBalance)
                .comments(event.getComment())
                .bookingDate(event.getBookingDate())
                .transactionDate(new Date())
                .debit(0)
                .credit(event.getAmount())
                .build();
        accountTransactionRepository.save(accountTransaction);
    }

    @Override
    public void on(DebitAccountEvent event) {
        var bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            return;
        }
        var currentBalance = bankAccount.get().getBalance();
        var latestBalance = currentBalance - event.getAmount();
        bankAccount.get().setBalance(latestBalance);
        accountRepository.save(bankAccount.get());

        var accountTransaction = AccountTransaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .accountId(event.getId())
                .transactionType(TransactionType.DEBIT)
                .bookingAmount(event.getAmount())
                .externalReferenceNumber(event.getExternalTransactionNumber())
                .referenceNumber(UUID.randomUUID().toString().toUpperCase().replaceAll("-","").substring(5,18))
                .balance(latestBalance)
                .comments(event.getComment())
                .bookingDate(event.getBookingDate())
                .transactionDate(new Date())
                .credit(0)
                .debit(event.getAmount())
                .build();
        accountTransactionRepository.save(accountTransaction);
    }

    @Override
    public void on(AccountCloseEvent event) {
        accountRepository.deleteById(event.getId());
    }
}
