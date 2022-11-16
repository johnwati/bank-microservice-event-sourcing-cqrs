package com.wati.account.query.api.queries;

import com.wati.cqrs.core.queries.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindTransactionByAccountId extends BaseQuery {
    private String accountId;
}
