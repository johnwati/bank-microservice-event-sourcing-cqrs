package com.wati.account.cmd.infastructure;

import com.wati.account.cmd.domain.AccountAggregate;
import com.wati.cqrs.core.domain.AggregateRoot;
import com.wati.cqrs.core.events.BaseEvent;
import com.wati.cqrs.core.handlers.EventSourcingHandlers;
import com.wati.cqrs.core.infrastructure.EventStore;
import com.wati.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service // make it a service provider and injectable
public class AccountEventSourcingHandler implements EventSourcingHandlers<AccountAggregate> {

    private final EventStore eventStore;

    private final EventProducer eventProducer;

    @Autowired
    public AccountEventSourcingHandler(EventStore eventStore, EventProducer eventProducer) {
        this.eventStore = eventStore;
        this.eventProducer = eventProducer;

    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if(events != null && !events.isEmpty()){
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }

        return aggregate;
    }

    @Override
    public void republishEvents() {
       var aggregateIds = eventStore.getAggregateId();
       for(var aggregateId: aggregateIds){
           var aggregate = getById(aggregateId);
           if(aggregate == null || !aggregate.getActive()) continue;
           var events = eventStore.getEvents(aggregateId);
           for(var event : events){
               eventProducer.produce(event.getClass().getSimpleName(), event);
           }
       }
    }
}
