package com.wati.account.query.api.queries;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.wati.cqrs.core.domain.BaseEntity;

import java.util.List;

public interface QueryHandler  {

    List<BaseEntity> handle(FindAllAccountsQuery query);
    List<BaseEntity> handle(FindAccountsByIdQuery query);
    List<BaseEntity> handle(FindAccountByHolderQuery query);
    List<BaseEntity> handle( FindAccountWithBalanceQuery query);
    List<BaseEntity> handle( FindAccountByAccountNumber query);
    List<BaseEntity> handle(FindTransactionByAccountId query);

}
