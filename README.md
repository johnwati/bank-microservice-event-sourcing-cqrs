# Bank Microservice Event-Sourcing CQRS
## Problem Statement
    Modeling a Bank Account and its TransactionsIn this example, we will have bank accounts, credit lines, and payments
    (debit and credit payments), and we always want to know the actual account balance and whether a pending debit would exceed the overdraft limit of a bank account. 
## Solution

### Architecture
![Alt text](Architecture+Overview.drawio.png?raw=true "Title")
### APIs
use Open Api (Swagger to test the APIS)
### Entities
#### Bank Account Entity
| Field Name            | Field Type                        | Description                              |   |   |
|-----------------------|-----------------------------------|------------------------------------------|---|---|
| id                    | string                            | account id identifier                    |   |   |
| accountHolder         | string                            | account holder name                      |   |   |
| accountNumber         | integer($int32)                   | account number                           |   |   |
| externalAccountNumber | integer($int32)                   | external account number for integrations |   |   |
| openingBalance        | number($double)                   | opening balance                          |   |   |
| creationDate          | string($date-time)                | creation date                            |   |   |
| closingDate           | string($date-time)                | account closing date                     |   |   |
| accountType           | string Enum: [ SAVINGS, CURRENT ] | account type                             |   |   |
| balance               | number($double)                   | current balance                          |   |   |
| overdraftLimit        | number($double)                   | overdraft limit                          |   |   |
| currency              | string                            | currency                                 |   |   |
|                       |                                   |                                          |   |   |
|                       |                                   |                                          |   |   |
#### Bank Account Transactions Entity
| Fields Name             | Field Type                     | Field Description                         |   |   |
|-------------------------|--------------------------------|-------------------------------------------|---|---|
| transactionId           | string                         | transaction ID                            |   |   |
| accountId               | string                         | account id                                |   |   |
| transactionDate         | string($date-time)             | transaction Date                          |   |   |
| bookingDate             | string($date-time)             | booking date                              |   |   |
| bookingAmount           | number($double)                | booking Amount                            |   |   |
| debit                   | number($double)                | debit                                     |   |   |
| credit                  | number($double)                | credit                                    |   |   |
| balance                 | number($double)                | current balance                           |   |   |
| transactionType         | string Enum: [ DEBIT, CREDIT ] | transaction type                          |   |   |
| comments                | string                         | transaction comment                       |   |   |
| referenceNumber         | string                         | reference number                          |   |   |
| externalReferenceNumber | string                         | external reference number for Integration |   |   |
### Deployments
#### Build
    Build project
        - mvn clean install


#### Run
    Run cammand
        - cd account.cmd
        - mvn spring-boot:run
    run Query
        - cd account.query
        - mvn spring-boot:run
    run docker( starts docker, kafka, mongo )
        - docker-compose up -d 
    
#### Access
| service                 | Url                                                              |
|-------------------------|------------------------------------------------------------------|
| Query Service API Doc   | http://localhost:5001/swagger-ui/index.html                      |
| Command Service API Doc | http://localhost:5002/swagger-ui/index.html                      |
| Mysql Adminer           | http://localhost:8080/?server=mysql&username=root                |
| MongoDb Express         | http://localhost:8081                                            |
| mysql                   | jdbc:mysql://localhost:3307/bankAccount                          |
| mongoDb                 | mongodb://xxxx:xxxx@localhost:27017/bankAccount?authSource=admin |
| kafka                   | localhost:9092                                                   |
|                         |                                                                  |
|                         |                                                                  |
|                         |                                                                  |
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
### Builder Design Patter
    Builder is a creational design pattern that lets you construct complex objects step by step. The pattern allows
    you to produce different types and representations of an object using the same construction code
### Domain Driven Design
    - Coined by Eric Evan in 2003
    - is an approach to structure and model software in a way that matches the business domain
    - places Primary Focus of a software project on the core area of the business
    - refers to problems as domains and aims to establish a common lang to talk about these problems
    - describes independent problems areas as Bounded Contexts
    - Bounded Context : is and indepeneded problem area.
        Describes a logical Boundary within which a prticular model is defined and applicalble
        Each bounded context correlates to a microservice eg: Bank Account Microservice# bank-microservice-event-sourcing-cqrs

#Improvements
    1. Change Query API from REST to GraphQL(for better searching and to reduce the number of APIS)
    2. Add Pagination to Rest API
    3. Add Accounting( Debit and Credit API)
    4. Add Negative tests

# Assumptions
    1. Accounting is done outside this application
