package com.wati.account.query.api.dto;

import com.wati.account.common.dto.BaseResponse;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class AccountBalanceLookupResponse  extends BaseResponse {
    private double balance;
    public AccountBalanceLookupResponse(String message){
        super(message);
    }
}
