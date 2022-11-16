package com.wati.account.query.domain.entity;

import com.wati.account.common.dto.TransactionStatus;
import com.wati.account.common.dto.TransactionType;
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
public class AccountTransaction  extends BaseEntity {
    @Id
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;
    private String accountId;
    private Date transactionDate;
    private Date bookingDate;
    private double bookingAmount;
    private double debit;
    private double credit;
    private double balance;
    private TransactionType transactionType;
    private String comments;
    private String referenceNumber;
    private String externalReferenceNumber;
//    private TransactionStatus transactionStatus;



}
