package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class EnableCommand extends Command{

    public EnableCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedClient(Tokens.ID_DEVELOPER);
        this.addNeededArg("Command");
        this.setHelpContent("Enables the command: reserved to bot's developer");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                ReformedSagiri.cm.getCommand(getArgs(event.getMessage().getFormattedContent())[0]).setEnabled(true);
                event.getChannel().sendMessage(getArgs(event.getMessage().getFormattedContent())[0]+" command successfully enabled");
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            event.getChannel().sendMessage("Error while attempting to enable command : command not found");
        }
    }
}
