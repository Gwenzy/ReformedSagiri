package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 18/11/2017.
 */
public class QueueCommand extends Command {

    public QueueCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedGroup("@everyone");
        this.setHelpContent("Shows current queue");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                if(MusicManager.getGuildAudioPlayer(event.getGuild()).getQueueDesc().equals(""))
                    event.getChannel().sendMessage("Queue is currently empty ;-;");
                else
                    event.getChannel().sendMessage(MusicManager.getGuildAudioPlayer(event.getGuild()).getQueueDesc());
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }


}
