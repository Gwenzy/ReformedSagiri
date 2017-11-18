package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.CommandMissingPermissionException;
import fr.gwenzy.bots.sagiri.exceptions.CommandWrongChannelException;
import fr.gwenzy.bots.sagiri.exceptions.MissingArgsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 16/11/2017.
 */
public class JoinCommand extends Command {

    public JoinCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.addAuthorizedGroup("Sagiri operator");
        this.setHelpContent("Makes the bot come on your channel");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().join();
                event.getChannel().sendMessage("Successfully joined "+event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel().getName());
            }
        } catch (NullPointerException e){
            event.getChannel().sendMessage("You must be in a vocal channel to do this command");
        } catch (SagiriException e) {
            e.printStackTrace();
        }

    }
}
