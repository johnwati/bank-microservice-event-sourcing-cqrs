package com.wati.account.query.api.queries;

import com.wati.account.query.api.dto.EqualityType;
import com.wati.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindAccountWithBalanceQuery extends BaseQuery {

    private EqualityType equalityType;
    private double balance;
}
