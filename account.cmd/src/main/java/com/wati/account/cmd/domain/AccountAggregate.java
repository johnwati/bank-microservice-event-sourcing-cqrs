package com.wati.account.cmd.domain;

import com.wati.account.cmd.api.commands.CreditAccountCommand;
import com.wati.account.cmd.api.commands.DebitAccountCommand;
import com.wati.account.cmd.api.commands.OpenAccountCommand;
import com.wati.account.common.dto.AccountType;
import com.wati.account.common.events.AccountCloseEvent;
import com.wati.account.common.events.AccountOpenedEvent;
import com.wati.account.common.events.CreditAccountEvent;
import com.wati.account.common.events.DebitAccountEvent;
import com.wati.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    private String accountHolder;
    private Integer accountNumber;
    private Integer externalAccountNumber;
    private AccountType accountType;
    private Double openingBalance;

    public double getBalance() {
        return this.balance;
    }
public Boolean getActive() {
        return  this.active;
}
    public AccountAggregate(OpenAccountCommand command) {
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .accountNumber(command.getAccountNumber())
                .externalAccountNumber(command.getExternalAccountNumber())
                .openingBalance(command.getOpeningBalance())
                .overdraftLimit(command.getOpeningBalance())
                .currency(command.getCurrency())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void creditAccount(CreditAccountCommand command) {
        if (!this.active) {
            throw new IllegalStateException("Cannot credit closed account!");
        }
        if(command.getBookingAmount() <= 0) {
            throw new IllegalStateException("Credit amount must be greater than 0!");
        }
        raiseEvent(CreditAccountEvent.builder()
                .id(this.id)
                .amount(command.getBookingAmount())
                .comment(command.getComment())
                .externalTransactionNumber(command.getExternalTransactionNumber())
                .bookingDate(new Date())
                .build());
    }

    public void apply(CreditAccountEvent event) {
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void debitAccount(DebitAccountCommand command) {
        if (!this.active) {
            throw new IllegalStateException("Cannot debit a closed account!");
        }

        raiseEvent(DebitAccountEvent.builder()
                .id(this.id)
                .amount(command.getBookingAmount())
                .comment(command.getComment())
                .externalTransactionNumber(command.getExternalTransactionNumber())
                .bookingDate(new Date())
                .build());
    }

    public void apply(DebitAccountEvent event) {
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalStateException("The bank account has already been closed!");
        }
        raiseEvent(AccountCloseEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountCloseEvent event) {
        this.id = event.getId();
        this.active = false;
    }
}
