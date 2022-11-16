package com.wati.account.cmd.api.commands;

import com.wati.account.common.dto.AccountType;
import com.wati.cqrs.core.commands.BaseCommand;
//import lombok.AllArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//import lombok.NoArgsConstructor;
//import com.techbank.account.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private Integer accountNumber;
    private Integer externalAccountNumber;
    private AccountType accountType;
    private Double openingBalance;
    private String currency;
    private Double overdraftLimit;

    public OpenAccountCommand(String id) {
        super(id);
    }
}