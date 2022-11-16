package com.wati.account.cmd.infastructure;

import com.wati.cqrs.core.commands.BaseCommand;
import com.wati.cqrs.core.commands.CommandHandlerMethod;
import com.wati.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher  implements CommandDispatcher {
    //represent a collection of registered command handler methods
    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes  = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        var handlers =  routes.computeIfAbsent(type, c->new LinkedList<>());
        handlers.add(handler);
    }
//     it's a dispatch method that sends given command to a registered command handler method
    @Override
    public void send(BaseCommand command) {
    var handlers = routes.get(command.getClass());
    if (handlers == null || handlers.size() == 0) {
        throw new RuntimeException("No command handler was registered!");
    }
    if (handlers.size() > 1) {
        throw new RuntimeException("Cannot send command to more than one handler!");
    }
    handlers.get(0).handle(command);
}
}
