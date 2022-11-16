package com.wati.account.cmd.infastructure;

import com.wati.account.cmd.domain.AccountAggregate;
import com.wati.account.cmd.domain.EventStoreRepository;
import com.wati.cqrs.core.Exceptions.AggregateNotFoundException;
import com.wati.cqrs.core.Exceptions.ConcurrencyException;
import com.wati.cqrs.core.events.BaseEvent;
import com.wati.cqrs.core.events.EventModel;
import com.wati.cqrs.core.infrastructure.EventStore;
import com.wati.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountEventStore  implements EventStore {
    private final EventStoreRepository eventStoreRepository;

    private final EventProducer eventProducer;

    @Autowired
    public AccountEventStore(EventStoreRepository eventStoreRepository, EventProducer eventProducer) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size() - 1).getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        var version = expectedVersion;
        for (var event: events) {
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timeStamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()) {
                eventProducer.produce(String.valueOf(event.getClass().getSimpleName()), (BaseEvent) event);
            }
        }
    }
    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null || eventStream.isEmpty()) {
            throw new AggregateNotFoundException("Incorrect account ID provided!");
        }
        return eventStream.stream().map(x -> x.getEventData()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAggregateId() {
        var eventStream = eventStoreRepository.findAll();
        if(eventStream == null || eventStream.isEmpty()){
            throw  new IllegalStateException("Could not retrieve event Stream from the event Store!");
        }

        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
    }
//    @Override
//    public List<String> getAggregateIds() {
//        var eventStream = eventStoreRepository.findAll();
//        if (eventStream == null || eventStream.isEmpty()) {
//            throw new IllegalStateException("Could not retrieve event stream from the event store!");
//        }
//        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().collect(Collectors.toList());
//    }
}
