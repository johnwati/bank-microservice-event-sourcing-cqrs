# Glossary
find some definitions of the important technologies used in this program.
### Message Types
    1. Command - intent to be done(a verb)
    2. events - object that descrbe something that has occured in application eg aggregate. when something 
        important occured within aggregate
    3. Queries

### Mediator Design Pattern
    - is Behavioral Design Pattern
    - promotes loose coupling by preventing Objects from referring to each other explicitly
    - simplifies communication between objects by introducing single obkect known as mediator that manages the 
    distibution of messages among other objects( messages are commands
### Aggregate
    Is the entity or group of entities that is always kept in consistent state. aggregate root is the entity within
    the aggregate that is responsible for maintaining this consistent state. this makes aggregate the primary building 
    block for implementing a command model in any CQRS based applications
### EventStore
    EventStore is a database that is used to store data as a sequence of immutable events over time and is the key 
    enabler of event sourcing
    key considerations.
        1. must be append only, no update or delete allowed
        2. each event saved should represent the version or state of an aggregate at any given point in time
        3. Events should be stored in chronological order and new events should be applied to the previouse event
        4. should implement optimistic concurrency control
### Repository Design Pattern
    Provides interface abstraction by which we will interact with event store or write database
    Mongo Db will be used as eventStore and to retrieve events for given aggregate.

### Domain Driven Design
    - Coined by Eric Evan in 2003
    - is an approach to structure and model software in a way that matches the business domain
    - places Primary Focus of a software project on the core area of the business
    - refers to problems as domains and aims to establish a common lang to talk about these problems
    - describes independent problems areas as Bounded Contexts
    - Bounded Context : is and indepeneded problem area.
        Describes a logical Boundary within which a prticular model is defined and applicalble
        Each bounded context correlates to a microservice eg: Bank Account Microservice# bank-microservice-event-sourcing-cqrs
# bank-microservice-event-sourcing-cqrs
