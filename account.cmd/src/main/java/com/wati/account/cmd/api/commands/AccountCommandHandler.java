package com.wati.account.cmd.api.commands;

import com.wati.account.cmd.domain.AccountAggregate;
import com.wati.cqrs.core.handlers.EventSourcingHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler{

    private final EventSourcingHandlers<AccountAggregate> eventSourcingHandlers;

    @Autowired
    public AccountCommandHandler(EventSourcingHandlers<AccountAggregate> eventSourcingHandlers) {
        this.eventSourcingHandlers = eventSourcingHandlers;
    }

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(CreditAccountCommand command) {
        var aggregate = eventSourcingHandlers.getById(command.getId());
        aggregate.creditAccount(command);
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(DebitAccountCommand command) {
        var aggregate = eventSourcingHandlers.getById(command.getId());
        if(command.getBookingAmount() > aggregate.getBalance()){
            throw new IllegalStateException("Debit declined, Insufficient dunds!");
        }
        aggregate.debitAccount(command);
        eventSourcingHandlers.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandlers.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandlers.save(aggregate);

    }

    @Override
    public void handle(RestoreReadDbCommand command) {
        eventSourcingHandlers.republishEvents();
    }
}
