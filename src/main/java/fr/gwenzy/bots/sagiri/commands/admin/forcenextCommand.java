package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.AliasAlreadyExistsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 19/11/2017.
 */
public class forcenextCommand extends Command {

    public forcenextCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedGroup("Sagiri operator");
        this.setHelpContent("Forces next music to play");
        try {
            this.addAlias("fn");
            this.addAlias("forceskip");
            this.addAlias("fs");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                MusicManager.getGuildAudioPlayer(event.getGuild()).next();
                event.getChannel().sendMessage("Successfully forced next music to play");
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
