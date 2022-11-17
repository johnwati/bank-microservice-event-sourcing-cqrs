package com.wati.account.cmd.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wati.account.cmd.api.commands.*;
import com.wati.account.common.dto.AccountType;
import com.wati.cqrs.core.infrastructure.CommandDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OpenAccountController.class)
class OpenAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommandDispatcher mockCommandDispatcher;

    @MockBean
    private  CommandHandler mockCommandHandler ;

    @BeforeEach
    void setup() {
        mockCommandDispatcher.registerHandler(OpenAccountCommand.class, mockCommandHandler::handle);
        mockCommandDispatcher.registerHandler(CreditAccountCommand.class, mockCommandHandler::handle);
        mockCommandDispatcher.registerHandler(DebitAccountCommand.class, mockCommandHandler::handle);
        mockCommandDispatcher.registerHandler(CloseAccountCommand.class, mockCommandHandler::handle);
        mockCommandDispatcher.registerHandler(RestoreReadDbCommand.class, mockCommandHandler::handle);
    }
    @Test
    void testOpenAccount() throws Exception {
        // Setup
        var command = new OpenAccountCommand();
        command.setAccountHolder("John Wati");
        command.setAccountType(AccountType.SAVINGS);
        command.setExternalAccountNumber(1100220033);
        command.setOpeningBalance(100.00);
        command.setCurrency("KSH");
        ObjectMapper obj  = new ObjectMapper();
        var jsonString = obj.writeValueAsString(command);
//        {
//            "accountHolder": "John Wati",
//                "accountNumber": 100202,
//                "externalAccountNumber": 10020,
//                "accountType": "SAVINGS",
//                "openingBalance": 100,
//                "currency": "KSH"
//        }
        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/openBankAccount")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        //TODO: Test response Content

    }
}
