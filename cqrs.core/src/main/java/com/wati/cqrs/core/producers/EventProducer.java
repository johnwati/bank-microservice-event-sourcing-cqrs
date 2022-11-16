package com.wati.cqrs.core.producers;

import com.wati.cqrs.core.events.BaseEvent;

public interface EventProducer  {
    void produce(String topic, BaseEvent event);

}
