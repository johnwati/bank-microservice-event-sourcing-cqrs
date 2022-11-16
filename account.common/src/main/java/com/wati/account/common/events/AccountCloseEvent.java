package com.wati.account.common.events;

import com.wati.cqrs.core.events.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountCloseEvent extends BaseEvent {



}

