package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 16/11/2017.
 */
public class VolumeCommand extends Command {

    public VolumeCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.addAuthorizedGroup("Sagiri operator");
        this.setHelpContent("Changes bot's volume");
        this.addNeededArg("volume");
        this.setNeedVoice(true);
        try {
            this.addAlias("vl");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                int volume = Integer.parseInt(getArgs(event.getMessage().getFormattedContent())[0]);
                int initial = MusicManager.getGuildAudioPlayer(event.getGuild()).getPlayer().getVolume();
                MusicManager.getGuildAudioPlayer(event.getGuild()).getPlayer().setVolume(volume);
                event.getChannel().sendMessage("Successfully changed volume from "+initial+" to "+volume);
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }

    }
}
