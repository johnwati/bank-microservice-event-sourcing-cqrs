package com.wati.account.query.domain.entity;

import com.wati.account.common.dto.AccountType;
import com.wati.cqrs.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BankAccount extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    private String accountHolder;
    private Integer accountNumber;
    private Integer externalAccountNumber;
    private double  openingBalance;
    private Date creationDate;
    private Date closingDate;
    private AccountType accountType;
    private double balance;
    private double overdraftLimit;
    private String currency;


}

