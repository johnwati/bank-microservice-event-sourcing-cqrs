//package com.wati.account.query.api.controllers;
//
//import com.wati.account.common.dto.AccountType;
//import com.wati.account.common.dto.TransactionType;
//import com.wati.account.query.QueryApplication;
//import com.wati.account.query.api.queries.*;
//import com.wati.account.query.domain.entity.AccountTransaction;
//import com.wati.account.query.domain.entity.BankAccount;
//import com.wati.cqrs.core.domain.BaseEntity;
//import com.wati.cqrs.core.infrastructure.QueryDispatcher;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(TransactionLookupController.class)
//@ContextConfiguration(classes= QueryApplication.class)
//class TransactionLookupControllerTest {
//
//
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    private MockMvc mockMvc;
//
//    @MockBean
//    private QueryDispatcher mockQueryDispatcher;
//
//    @MockBean
//    private  QueryHandler mockQueryHandler ;
//
//    @BeforeEach
//    void setUp(){
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        mockQueryDispatcher.registerHandler(FindAllAccountsQuery.class, mockQueryHandler::handle);
//        mockQueryDispatcher.registerHandler(FindAccountsByIdQuery.class, mockQueryHandler::handle);
//        mockQueryDispatcher.registerHandler(FindAccountByHolderQuery.class, mockQueryHandler::handle);
//        mockQueryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, mockQueryHandler::handle);
//        mockQueryDispatcher.registerHandler(FindAccountByAccountNumber.class, mockQueryHandler::handle);
//        mockQueryDispatcher.registerHandler(FindTransactionByAccountId.class,mockQueryHandler::handle);
//    }
//////
//    @Test
//    void testFindTransactionsByAccountId() throws Exception {
//        // Setup
//        // Configure QueryDispatcher.send(...).
//        final List<AccountTransaction> accountTransactions = List.of(
//                new AccountTransaction(
//                        "transactionId",
//                        "e1c00771-e01b-4555-a6e1-d974c2f6ecce",
//                        new Date(), new Date(), 0.0, 0.0, 0.0, 0.0,
//                        TransactionType.DEBIT, "comments", "referenceNumber", "externalReferenceNumber"));
//        List<BaseEntity> accountTransactionList = new ArrayList<>(accountTransactions);
//        when(mockQueryDispatcher.send(new FindTransactionByAccountId("e1c00771-e01b-4555-a6e1-d974c2f6ecce"))).thenReturn(accountTransactionList);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountId/{id}", "id")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
////        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
////
//    @Test
//    void testFindTransactionsByAccountId_QueryDispatcherReturnsNoItems() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindTransactionByAccountId("e1c00771-e01b-4555-a6e1-d974c2f6ecce"))).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountId/{id}", "e1c00771-e01b-4555-a6e1-d974c2f6ecce")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
////        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//
//
//
//
//    @Test
//    void testCheckOverdraftLimit() throws Exception {
//        // Setup
//        // Configure QueryDispatcher.send(...).
//        final List<BankAccount> accountTransactions = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
//                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
//                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
//        List<BaseEntity> accountTransactionList = new ArrayList<>();
//        accountTransactions.forEach(accountTransactionList::add);
//        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(accountTransactionList);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/checkOverdraftLimit/{accountNumber}/{debitAmount}", 0, 0)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testCheckOverdraftLimit_QueryDispatcherReturnsNoItems() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/checkOverdraftLimit/{accountNumber}/{debitAmount}", 0, 0)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testCheckOverdraftLimit_QueryDispatcherReturnsNull() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(null);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/checkOverdraftLimit/{accountNumber}/{debitAmount}", 0, 0)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testFindTransactionsByAccountId1() throws Exception {
//        // Setup
//        // Configure QueryDispatcher.send(...).
//        final List<AccountTransaction> accountTransactions = List.of(
//                new AccountTransaction("transactionId", "accountId",
//                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
//                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0.0, 0.0, 0.0, 0.0,
//                        TransactionType.DEBIT, "comments", "referenceNumber", "externalReferenceNumber"));
//        List<BaseEntity> accountTransactionList = new ArrayList<>();
//        accountTransactions.forEach(accountTransactionList::add);
//        when(mockQueryDispatcher.send(new FindTransactionByAccountId("id"))).thenReturn(accountTransactionList);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountId/{id}", "id")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testFindTransactionsByAccountId_QueryDispatcherReturnsNoItems1() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindTransactionByAccountId("id"))).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountId/{id}", "id")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testFindTransactionsByAccountId_QueryDispatcherReturnsNull() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindTransactionByAccountId("id"))).thenReturn(null);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountId/{id}", "id")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testFindTransactionsByAccountNumber() throws Exception {
//        // Setup
//        // Configure QueryDispatcher.send(...).
//        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
//                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
//                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
//        List<BaseEntity> accountTransactionList = new ArrayList<>();
//        accountTransactionList.addAll(bankAccounts);
//        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(accountTransactionList);
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountNumber/{accountNumber}", 0)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//
//    @Test
//    void testFindTransactionsByAccountNumber_QueryDispatcherReturnsNoItems() throws Exception {
//        // Setup
//        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final MockHttpServletResponse response = mockMvc.perform(
//                        get("/api/v1/transactionLookup/findByAccountNumber/{accountNumber}", 0)
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//
//        // Verify the results
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
//    }
//}
