package com.wati.account.cmd.infastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wati.cqrs.core.events.BaseEvent;
import com.wati.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountEventProducer implements EventProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    ObjectMapper mapper = new ObjectMapper();


    @Override
    public void produce(String topic, BaseEvent event) {
//        this.kafkaTemplate.send(topic, event);
        System.out.println(event.toString());
        try {
            String json = mapper.writeValueAsString(event);
            this.kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}

