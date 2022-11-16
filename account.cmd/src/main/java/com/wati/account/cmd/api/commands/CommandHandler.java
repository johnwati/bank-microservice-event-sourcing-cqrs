package com.wati.account.cmd.api.commands;



public interface CommandHandler {

    void handle(OpenAccountCommand command);
    void handle(CreditAccountCommand command);
    void handle(DebitAccountCommand command);
    void handle(CloseAccountCommand command);
    void handle(RestoreReadDbCommand command);
}
