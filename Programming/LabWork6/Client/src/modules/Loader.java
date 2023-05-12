package modules;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import common.CommandDescription;
import managers.AbstractLoader;
import managers.BaseTextReceiver;

public class Loader extends AbstractLoader {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CommandDescription parseCommand(String command, Map<String, CommandDescription> commandDescriptionMap) {
        String[] commandParts = command.split(" ");
        if (commandParts.length == 0) {
            throw new RuntimeException("Command is empty!");
        }
        if (commandDescriptionMap.containsKey(commandParts[0])) {
            CommandDescription commandDescription = commandDescriptionMap.get(commandParts[0]);
            if (commandDescription.getOneLineArguments().size() != commandParts.length - 1) {
                throw new RuntimeException("Wrong number of arguments!");
            }
            for (int i = 0; i < commandDescription.getOneLineArguments().size(); i++) {
                if (!commandDescription.getOneLineArguments().get(i).equals(commandParts[i + 1])) {
                    throw new RuntimeException("Wrong argument!");
                }
            }
        } else {
            throw new RuntimeException("Unknown command!");
        }
        return commandDescriptionMap.get(commandParts[0]);
    }

    @Override
    public <T> T enterWrapper(String message, Class<T> type, BaseTextReceiver textReceiver){
        textReceiver.print(message);
        try {
            return (T) reader.readLine().valueOf(type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String enterString(String message, BaseTextReceiver textReceiver){
        textReceiver.print(message);
        try {
            return reader.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
