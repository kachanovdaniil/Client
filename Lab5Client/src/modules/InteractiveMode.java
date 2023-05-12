package modules;

import caller.Callable;
import common.CommandDescription;
import modules.CallableManager;
import managers.LoadDescription;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.List;
import java.util.Map;

public class InteractiveMode {
    private final CallableManager callableManager;
    private TextReceiver textReceiver;
    private Loader loader;

    private ObjectSender objectSender;

    private Map<String, CommandDescription> commandDescriptionMap;

    private RequestHandler requestHandler;

    public InteractiveMode(TextReceiver textReceiver, Loader loader, RequestHandler requestHandler, ObjectSender objectSender, CallableManager callableManager) {
        this.textReceiver = textReceiver;
        this.loader = loader;
        this.requestHandler = requestHandler;
        this.callableManager = callableManager;

    }

    private Map<String, CommandDescription> getCommandDescriptionMap() {
        for(int i = 0; i < 5; i++) {
            try {
                DatagramPacket MapOfCommands = requestHandler.receivePacketWithTimeout();
                break;
            }catch (Exception e){
                textReceiver.print("Error while receiving map of commands, error with server connection.\n Wait...");
            }
        }
        //десериализовать и получить карту коллекции из MapOfCommands
        return null;

    }

    public void start() {
        textReceiver.print("Interactive mode started! Check command help to see available commands.");
        Map<String, CommandDescription> commandDescriptionMap = getCommandDescriptionMap();
        if(!commandDescriptionMap.containsKey("exit")){
            commandDescriptionMap.put("exit", new CommandDescription.Builder()
            .setOneLineArguments(List.of(new String[]{}))
                    .setName("exit")
                    .setObjectArgument(List.of(new Object[]{}))
                    .setCaller(new Callable() {
                        @Override
                        public void call() {
                            System.exit(0);
                        })
                    }
        while (true){
            String in = loader.enterString("Enter command: ", textReceiver);
            CommandDescription command = loader.parseCommand(in, commandDescriptionMap);
            callableManager.add((Callable) command);
            callableManager.callAll();
            callableManager.clear();
        }
    }

}
