package com.wati.account.query.infastructure.consumers;

import com.wati.account.common.events.AccountCloseEvent;
import com.wati.account.common.events.AccountOpenedEvent;
import com.wati.account.common.events.CreditAccountEvent;
import com.wati.account.common.events.DebitAccountEvent;
import com.wati.account.query.infastructure.handlers.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer{


    private final EventHandler eventHandler;


    @Autowired
    public AccountEventConsumer(EventHandler eventHandler) {

        this.eventHandler = eventHandler;
    }


    @KafkaListener(
            topics = "AccountOpenedEvent",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=com.wati.account.common.events.AccountOpenedEvent"}
    )
    @Override
    public void consume(@Payload AccountOpenedEvent event, Acknowledgment ock) {
        eventHandler.on(event);
        ock.acknowledge();
    }


    @KafkaListener(
            topics = "CreditAccountEvent",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=com.wati.account.common.events.CreditAccountEvent"}
    )
    @Override
    public void consume(@Payload CreditAccountEvent event, Acknowledgment ock) {
        eventHandler.on(event);
        ock.acknowledge();
    }

    @KafkaListener(topics = "DebitAccountEvent",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=com.wati.account.common.events.DebitAccountEvent"}
    )
    @Override
    public void consume(@Payload DebitAccountEvent event, Acknowledgment ock) {
        eventHandler.on(event);
        ock.acknowledge();
    }

    @KafkaListener(
            topics = "AccountCloseEvent",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=com.wati.account.common.events.AccountCloseEvent"}
    )
    @Override
    public void consume(@Payload AccountCloseEvent event, Acknowledgment ock) {
        eventHandler.on(event);
        ock.acknowledge();
    }
}
