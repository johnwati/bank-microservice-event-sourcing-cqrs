package com.wati.account.query.infastructure.handlers;

import com.wati.account.common.events.AccountCloseEvent;
import com.wati.account.common.events.AccountOpenedEvent;
import com.wati.account.common.events.CreditAccountEvent;
import com.wati.account.common.events.DebitAccountEvent;

public interface EventHandler {

    void on(AccountOpenedEvent event);
    void on(CreditAccountEvent event);
    void on(DebitAccountEvent event);
    void on(AccountCloseEvent event);
}
