package com.wati.cqrs.core.infrastructure;

import com.wati.cqrs.core.commands.BaseCommand;
import com.wati.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handle);
    void send(BaseCommand command);
}
