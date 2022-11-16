package com.wati.cqrs.core.Exceptions;

public class AggregateNotFoundException  extends RuntimeException{
    public AggregateNotFoundException(String message){
        super(message);
    }
}
