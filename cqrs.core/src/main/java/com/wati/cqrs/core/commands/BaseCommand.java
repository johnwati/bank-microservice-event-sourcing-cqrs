package com.wati.cqrs.core.commands;

import com.wati.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public abstract class BaseCommand  extends Message {

   public BaseCommand(String id){
        super(id);
    }

}