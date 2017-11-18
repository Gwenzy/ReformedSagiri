package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.commands.CommandManager;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class DisableCommand extends Command{

    public DisableCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedClient(Tokens.ID_DEVELOPER);
        this.addNeededArg("Command");
        this.setHelpContent("Disables the command: reserved to bot's developer");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                ReformedSagiri.cm.getCommand(getArgs(event.getMessage().getFormattedContent())[0]).setEnabled(false);
                event.getChannel().sendMessage(getArgs(event.getMessage().getFormattedContent())[0]+" command successfully disabled");
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            event.getChannel().sendMessage("Error while attempting to disable command : command not found");
        }
    }
}
