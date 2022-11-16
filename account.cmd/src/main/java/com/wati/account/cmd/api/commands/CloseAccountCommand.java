package com.wati.account.cmd.api.commands;

import com.wati.cqrs.core.commands.BaseCommand;

public class CloseAccountCommand  extends BaseCommand {
    public CloseAccountCommand(String id){
        super(id);
    }
}
