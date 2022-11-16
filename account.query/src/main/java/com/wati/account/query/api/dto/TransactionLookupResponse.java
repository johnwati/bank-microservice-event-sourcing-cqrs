package com.wati.account.query.api.dto;

import com.wati.account.common.dto.BaseResponse;
import com.wati.account.query.domain.entity.AccountTransaction;
import com.wati.account.query.domain.entity.BankAccount;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class TransactionLookupResponse extends BaseResponse {
    private List<AccountTransaction> transactions;

    public TransactionLookupResponse(String message) {
        super(message);

    }

}
