package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.AliasAlreadyExistsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 19/11/2017.
 */
public class RepeatCommand extends Command {

    public RepeatCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedGroup("Sagiri operator");
        this.setHelpContent("Sets repeat mode");
        this.setNeedVoice(true);
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                MusicManager.getGuildAudioPlayer(event.getGuild()).setRepeatMode(!MusicManager.getGuildAudioPlayer(event.getGuild()).isRepeatMode());

                event.getChannel().sendMessage("Repeat mode is now "+(MusicManager.getGuildAudioPlayer(event.getGuild()).isRepeatMode()?"enabled":"disabled"));
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
