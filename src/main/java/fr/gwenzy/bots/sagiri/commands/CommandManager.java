package fr.gwenzy.bots.sagiri.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwend on 14/11/2017.
 */
public class CommandManager {
    private List<Command> commands;

    public CommandManager(){
        this.commands = new ArrayList<>();

    }

    public void registerCommand(Command command){
            this.commands.add(command);
    }

    public List<Command> getCommands(){
        return this.commands;
    }
    public List<String> getTextCommands(){
        List<String> textCommands = new ArrayList<>();
        for(Command c : getCommands()){
            textCommands.add(c.getName());
            textCommands.addAll(c.getAliases());
        }

        return textCommands;
    }

    public Command getCommand(String name){
        for(Command c : getCommands()){
            if(name.equals(c.getName()))
                return c;
        }

        return null;
    }

}
