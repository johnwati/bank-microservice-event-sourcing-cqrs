package com.wati.cqrs.core.handlers;

import com.wati.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandlers<T> {

    void  save(AggregateRoot aggregate);
    T getById(String id);

    void republishEvents();
}
