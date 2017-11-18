package fr.gwenzy.bots.sagiri.commands.everyone;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.music.AudioProvider;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import fr.gwenzy.bots.sagiri.commands.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by gwend on 15/11/2017.
 */
public class PlayCommand extends Command {
    public PlayCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.setHelpContent("Plays a song : URL can be YouTube URL, Youtube video ID and a lot more !");
        this.addNeededArg("URL");
        this.addAuthorizedGroup("@everyone");
        this.setNeedVoice(true);


    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)) {
                String url = getArgMessage(event.getMessage().getFormattedContent(), 0);
                if(url.startsWith("#")){
                    int ID = Integer.parseInt(url.split("#")[1]);
                    if(ID>0 && ID<6) {
                        url = MusicManager.getGuildAudioPlayer(event.getGuild()).getLastSearches().get(event.getAuthor().getLongID()).get(ID).split("!;;!")[1];

                    }
                    else
                        throw new NumberFormatException();

                }
                MusicManager.loadAndPlay(event.getChannel(), url, event.getAuthor().getLongID(), event.getGuild());
                if(MusicManager.getGuildAudioPlayer(event.getGuild()).isSneakyMode())
                    event.getMessage().delete();
            }
        }  catch (SagiriException e) {
            e.printStackTrace();
        } catch(NumberFormatException e){
            event.getChannel().sendMessage("No video found with this ID, try >search before (see >help search)");
        }
    }
}
