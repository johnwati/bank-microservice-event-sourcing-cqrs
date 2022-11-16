package com.wati.account.cmd.api.commands;

import com.wati.cqrs.core.commands.BaseCommand;
import lombok.Data;

import java.util.Date;

@Data
public class DebitAccountCommand extends BaseCommand {
    private double bookingAmount;
    private Date bookingDate;
    private String comment;
    private String externalTransactionNumber;
}
