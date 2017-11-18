package fr.gwenzy.bots.sagiri.commands.everyone;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class InfosCommand extends Command{

    public InfosCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedGroup("@everyone");
        this.setHelpContent("Gives information about the song currently played by the bot");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                try {
                    AudioTrackInfo infos = MusicManager.getGuildAudioPlayer(event.getGuild()).getPlayer().getPlayingTrack().getInfo();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.withColor(Color.CYAN);
                    eb.withTitle("Track informations");
                    eb.withDesc("------------------------------------------------------------------");
                    eb.appendField("Title", infos.title, false);
                    eb.appendField("Youtube Channel", infos.author, false);
                    eb.appendField("Duration", (infos.length/1000)+"s", false);
                    eb.appendField("URL", infos.uri, false);
                    long startingTimestamp = MusicManager.getGuildAudioPlayer(event.getGuild()).getStartingTimestamp();
                    eb.appendField("Progress", ""+((System.currentTimeMillis()-startingTimestamp)/1000)+"/"+(infos.length/1000)+"s - "+Math.round(((double)System.currentTimeMillis()-(double)startingTimestamp)/(double)infos.length*100)+"% - "+((infos.length-System.currentTimeMillis()+startingTimestamp)/1000)+"s left" , false);
                    eb.appendField("User who added this music", ReformedSagiri.logged.getUserByID(MusicManager.getGuildAudioPlayer(event.getGuild()).getCurrentAuthor()).mention()+"", false);
                    event.getChannel().sendMessage(eb.build());
                    event.getMessage().delete();



                }catch(Exception e){
                    event.getChannel().sendMessage("No song is currently playing");
                }
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
