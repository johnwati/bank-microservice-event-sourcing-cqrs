//package com.techbank.account.cmd.api.controllers;
//
//import CreditAccountCommand;
//import OpenAccountResponse;
//import BaseResponse;
//import AggregateNotFoundException;
//import CommandDispatcher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.text.MessageFormat;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@RestController
//@RequestMapping(path = "/api/v1/depositFunds")
//public class DepositFundController {
//
//    private final Logger logger = Logger.getLogger(DepositFundController.class.getName());
//
//    private final CommandDispatcher commandDispatcher;
//
//    @Autowired
//    public DepositFundController(CommandDispatcher commandDispatcher) {
//
//        this.commandDispatcher = commandDispatcher;
//    }
//
//    @PutMapping
//    public ResponseEntity<BaseResponse> deposit(@PathVariable(value = "id") String id,
//                                                @RequestBody CreditAccountCommand command){
//        try{
//            command.setId(id);
//            commandDispatcher.send(command);
//
//            return new ResponseEntity<>(new BaseResponse("Deposit Funds Request completed Successfully"), HttpStatus.OK);
//        }catch(IllegalStateException | AggregateNotFoundException e ){
//            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request- {0}.",e.toString()));
//            return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
//
//        }catch(Exception e){
//            var safeErrorMessage = MessageFormat.format("Error while processing request to  deposit funds to Bank" +
//                    "  Account with id - {0}.",id);
//            logger.log(Level.SEVERE,safeErrorMessage,e);
//            return new ResponseEntity<>(new BaseResponse(safeErrorMessage),HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//}
