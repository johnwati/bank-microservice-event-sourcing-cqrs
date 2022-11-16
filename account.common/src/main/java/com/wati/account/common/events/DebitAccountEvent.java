package com.wati.account.common.events;


import com.wati.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DebitAccountEvent extends BaseEvent {
    private  double amount;
    private Date bookingDate;
    private String comment;
    private String externalTransactionNumber;

}