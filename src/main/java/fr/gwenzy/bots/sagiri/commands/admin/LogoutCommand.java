package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by gwend on 14/11/2017.
 */
public class LogoutCommand extends Command {
    public LogoutCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        try {
            this.addAlias("disconnect");
            this.addAuthorizedClient(Tokens.ID_DEVELOPER);
            this.setHelpContent("Makes the bot disconnect : reserved to bot's developer");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                event.getChannel().sendMessage("Hey ! I'm leaving now, see you soon ;-)");
                ReformedSagiri.logged.logout();
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
