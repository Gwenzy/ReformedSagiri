package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class SneakyCommand extends Command{

    public SneakyCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedGroup("Sagiri operator");
        this.setHelpContent("Enable/Disable sneaky mode for guild (Allows playing musics without log)");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                MusicManager.getGuildAudioPlayer(event.getGuild()).setSneakyMode(!MusicManager.getGuildAudioPlayer(event.getGuild()).isSneakyMode());
                event.getChannel().sendMessage("Sneaky mode is now "+(MusicManager.getGuildAudioPlayer(event.getGuild()).isSneakyMode()?"enabled":"disabled"));
                }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
