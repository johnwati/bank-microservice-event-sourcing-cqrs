package com.wati.account.common.events;

import com.wati.account.common.dto.AccountType;
import com.wati.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountOpenedEvent extends BaseEvent  {

    private String accountHolder;
    private Integer accountNumber;
    private Integer externalAccountNumber;
    private AccountType accountType;
    private Double openingBalance;
    private Date createdDate;
    private String currency;
    private Double overdraftLimit;


}
//{"id":"9c0cc2ad-d7d3-44ec-9a42-6935584d788a","version":0,"accountHolder":"John Doe","accountType":"SAVINGS","createdDate":1668399602209,"openingBalance":50.0}