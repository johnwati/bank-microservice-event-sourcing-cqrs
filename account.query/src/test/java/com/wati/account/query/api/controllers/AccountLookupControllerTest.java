package com.wati.account.query.api.controllers;

import com.wati.account.common.dto.AccountType;
import com.wati.account.query.api.dto.EqualityType;
import com.wati.account.query.api.queries.*;
import com.wati.account.query.domain.entity.BankAccount;
import com.wati.account.query.domain.repository.AccountRepository;
import com.wati.cqrs.core.domain.BaseEntity;
import com.wati.cqrs.core.infrastructure.QueryDispatcher;
import com.wati.cqrs.core.queries.BaseQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountLookupController.class)
class AccountLookupControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private QueryDispatcher mockQueryDispatcher;

    @MockBean
    private QueryHandler mockQueryHandler;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockQueryDispatcher.registerHandler(FindAllAccountsQuery.class, mockQueryHandler::handle);
        mockQueryDispatcher.registerHandler(FindAccountsByIdQuery.class, mockQueryHandler::handle);
        mockQueryDispatcher.registerHandler(FindAccountByHolderQuery.class, mockQueryHandler::handle);
        mockQueryDispatcher.registerHandler(FindAccountWithBalanceQuery.class, mockQueryHandler::handle);
        mockQueryDispatcher.registerHandler(FindAccountByAccountNumber.class, mockQueryHandler::handle);
        mockQueryDispatcher.registerHandler(FindTransactionByAccountId.class, mockQueryHandler::handle);
    }


    @Test
    void testGetAccountById() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("e1c00771-e01b-4555-a6e1-d974c2f6ecce", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));

//        Iterable<BankAccount> bankAccounts = accountRepository.save(bankAccountsi.get(0));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(new FindAllAccountsQuery())).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/", "e1c00771-e01b-4555-a6e1-d974c2f6ecce")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountBalance() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        var bankAccount = accountRepository.findById("100202");

        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountBalance/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountBalance_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountBalance/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountBalance_QueryDispatcherReturnsNull() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountBalance/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountByHolder() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(new FindAccountByHolderQuery("accountHolder"))).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/byHolder/{accountHolder}", "accountHolder")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountByHolder_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByHolderQuery("accountHolder")))
                .thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/byHolder/{accountHolder}", "accountHolder")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountByHolder_QueryDispatcherReturnsNull() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByHolderQuery("accountHolder"))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/byHolder/{accountHolder}", "accountHolder")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountById1() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(new FindAccountsByIdQuery("id"))).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/byId/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountById_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountsByIdQuery("id"))).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/byId/{id}", "id")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountWithBalance() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(new FindAccountWithBalanceQuery(EqualityType.GREATER_THAN, 0.0))).thenReturn(
                bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/withBalance/{equalityType}/{balance}", "equalityType", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountWithBalance_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountWithBalanceQuery(EqualityType.GREATER_THAN, 0.0)))
                .thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/withBalance/{equalityType}/{balance}", "equalityType", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAccountWithBalance_QueryDispatcherReturnsNull() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountWithBalanceQuery(EqualityType.GREATER_THAN, 0.0)))
                .thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/withBalance/{equalityType}/{balance}", "equalityType", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllAccount() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(any(BaseQuery.class))).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllAccount_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(any(BaseQuery.class))).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
//        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetAllAccount_QueryDispatcherReturnsNull() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(any(BaseQuery.class))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/bankAccountLookup/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetByAccountNumber() throws Exception {
        // Setup
        // Configure QueryDispatcher.send(...).
        final List<BankAccount> bankAccounts = List.of(new BankAccount("id", "accountHolder", 0, 0, 0.0,
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), AccountType.SAVINGS, 0.0, 0.0, "currency"));
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        bankAccounts.forEach(accountRepository::save);
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(bankAccountList);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountByAccountNumber/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetByAccountNumber_QueryDispatcherReturnsNoItems() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountByAccountNumber/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetByAccountNumber_QueryDispatcherReturnsNull() throws Exception {
        // Setup
        when(mockQueryDispatcher.send(new FindAccountByAccountNumber(0))).thenReturn(null);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(
                        get("/api/v1/bankAccountLookup/getAccountByAccountNumber/{accountNumber}", 0)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}


