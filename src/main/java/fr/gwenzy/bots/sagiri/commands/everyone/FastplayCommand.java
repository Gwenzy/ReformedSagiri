package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import fr.gwenzy.bots.sagiri.music.youtube.Search;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class FastplayCommand extends Command {


    public FastplayCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.addAuthorizedGroup("@everyone");
        this.addNeededArg("Keywords");
        this.setHelpContent("Will play the first result found on YouTube with your keywords");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){

                String keywords = getArgMessage(event.getMessage().getFormattedContent(), 0);
                List<String> results = null;

                try {
                    results = new Search().search(keywords);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    MusicManager.loadAndPlay(event.getChannel(), results.get(0).split("!;;!")[1], event.getAuthor().getLongID(), event.getGuild());

                    if(MusicManager.getGuildAudioPlayer(event.getGuild()).isSneakyMode()) {
                        event.getMessage().delete();
                    }
                } catch (Exception e) {
                    event.getChannel().sendMessage("No results");
                }
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
