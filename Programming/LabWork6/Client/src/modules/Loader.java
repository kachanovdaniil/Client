package modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import common.CommandDescription;
import managers.AbstractLoader;
import managers.BaseTextReceiver;
import managers.LoadDescription;

public class Loader extends AbstractLoader {

    private static final Map<Class<? extends Number>, Function<String, ? extends Number>> PARSERS = new HashMap<>();

    static {
        PARSERS.put(Integer.class, Integer::valueOf);
        PARSERS.put(Long.class, Long::valueOf);
        PARSERS.put(Float.class, Float::valueOf);
        PARSERS.put(Double.class, Double::valueOf);
        PARSERS.put(Byte.class, Byte::valueOf);
        PARSERS.put(Short.class, Short::valueOf);
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CommandDescription parseCommand(Map<String, CommandDescription> commandDescriptionMap, String command) {
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
            return commandDescription;
        } else {
            throw new RuntimeException("Unknown command!");
        }
    }


    public <T extends LoadDescription<Enum>> T enterEnum(String s, T t, BaseTextReceiver baseTextReceiver) {
        baseTextReceiver.print(s);
        try {
            String line = reader.readLine();
            return (T)t.setValue(Enum.valueOf((Class<Enum>)t.getType(), line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends LoadDescription<Number>> T enterWrapper(String s, T t, BaseTextReceiver baseTextReceiver) {
        baseTextReceiver.print(s);
        try {
            return (T)t.setValue(PARSERS.get(t.getType()).apply(reader.readLine()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LoadDescription<String> enterString(String s, LoadDescription<String> description, BaseTextReceiver baseTextReceiver) {
        baseTextReceiver.print(s);
        try {
            return description.setValue(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}