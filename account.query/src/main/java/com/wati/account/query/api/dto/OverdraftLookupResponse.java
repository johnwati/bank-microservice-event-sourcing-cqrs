package com.wati.account.query.api.dto;

import com.wati.account.common.dto.BaseResponse;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class OverdraftLookupResponse  extends BaseResponse {

    Boolean overdraftLimitExceeded;
    public OverdraftLookupResponse(String message) {
        super(message);
    }
}
