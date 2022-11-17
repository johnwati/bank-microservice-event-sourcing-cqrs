package com.wati.account.cmd.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wati.account.cmd.api.commands.*;
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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DebitAccountController.class)
class DebitAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    @MockBean
    private CommandDispatcher mockCommandDispatcher;

    @Test
    void testDebitAccount() throws Exception {
        // Setup
        var id = "e1c00771-e01b-4555-a6e1-d974c2f6ecce";
        var command  = new DebitAccountCommand();
        command.setBookingAmount(100);
        command.setBookingDate(new Date());
        command.setComment("Test Debit Transaction");
        command.setExternalTransactionNumber("xvsk30JSJS");
//        {
//            "bookingAmount": 10,
//                "bookingDate": "2022-11-15T21:05:32.978Z",
//                "comment": "School fees payment",
//                "externalTransactionNumber": "xvsk30JSJS"
//        }
        // Run the test
        ObjectMapper obj  = new ObjectMapper();
        var jsonString = obj.writeValueAsString(command);
        final MockHttpServletResponse response = mockMvc.perform(put("/api/v1/debitAccount/"+id, "id")
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        //TODO: Test response Content

    }
}
