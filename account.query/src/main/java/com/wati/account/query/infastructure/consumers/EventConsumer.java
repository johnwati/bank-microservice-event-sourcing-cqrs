package com.wati.account.query.infastructure.consumers;


import com.wati.account.common.events.AccountCloseEvent;
import com.wati.account.common.events.AccountOpenedEvent;
import com.wati.account.common.events.DebitAccountEvent;
import com.wati.account.common.events.CreditAccountEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
    void consume(@Payload AccountOpenedEvent event, Acknowledgment ock);

    void consume(@Payload CreditAccountEvent event, Acknowledgment ock);

    void consume(@Payload DebitAccountEvent event, Acknowledgment ock);

    void consume(@Payload AccountCloseEvent event, Acknowledgment ock);



}
