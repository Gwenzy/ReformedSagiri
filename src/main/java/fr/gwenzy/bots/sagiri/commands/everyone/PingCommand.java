package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by gwend on 14/11/2017.
 */
public class PingCommand extends Command {
    public PingCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        try {
            this.addAlias("pg");
            this.addAuthorizedGroup("@everyone");
            this.setHelpContent("Send a \"Pong\" if your message is correctly received by the bot");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                event.getChannel().sendMessage("Pong");
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
