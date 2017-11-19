package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.AliasAlreadyExistsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by Shû~ on 19/11/2017.
 */
public class NextCommand extends Command {

    public NextCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.setHelpContent("Requests the next music, music will be skipped if at least 50% of voice channel users asked to skip");
        this.addAuthorizedGroup("@everyone");
        this.setNeedVoice(true);
        try {
            this.addAlias("skip");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                if(MusicManager.getGuildAudioPlayer(event.getGuild()).askNext(event.getAuthor().getLongID(), event.getGuild().getConnectedVoiceChannel())){
                    event.getChannel().sendMessage("OK ! Current music has been skipped");
                }
                else
                    event.getChannel().sendMessage("Next request received, "+MusicManager.getGuildAudioPlayer(event.getGuild()).getWantToSkip(event.getGuild().getConnectedVoiceChannel())+" people want to skip this music");
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
