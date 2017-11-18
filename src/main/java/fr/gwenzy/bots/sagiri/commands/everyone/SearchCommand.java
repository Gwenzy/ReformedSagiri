package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import fr.gwenzy.bots.sagiri.music.youtube.Search;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class SearchCommand extends Command {

    public SearchCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.addAuthorizedGroup("@everyone");
        this.addNeededArg("Keywords");
        this.setHelpContent("Will display the 5 firsts results found on YouTube");
    }

    @Override
    public void handle(MessageReceivedEvent event ){
        try {
            if(canBeExecuted(event)) {
                String query = getArgMessage(event.getMessage().getFormattedContent(), 0);
                List <String> results = null;
                try {
                    results = new Search().search(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int id = 1;
                if (results.size()==0)
                    event.getChannel().sendMessage("No results for: "+query);
                for (String str : results) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.withColor(Color.CYAN);
                    eb.withTitle("Query result | ID #" + id);
                    eb.withDesc("------------------------------------------------------------------");

                    String title = str.split("!;;!")[0];
                    String videoID = str.split("!;;!")[1];
                    String duration = str.split("!;;!")[2];
                    String channel = str.split("!;;!")[3];
                    String thumbnailURL = str.split("!;;!")[4];

                    PeriodFormatter formatter = ISOPeriodFormat.standard();
                    Period p = formatter.parsePeriod(duration);
                    duration = String.format("%02d", p.getMinutes()) + ":" + String.format("%02d", p.getSeconds());


                    eb.appendField("Title", title, false);
                    eb.appendField("ID", videoID, false);

                    eb.appendField("Duration", duration, false);
                    eb.appendField("Channel", channel, false);
                    eb.withThumbnail(thumbnailURL);

                    MusicManager.getGuildAudioPlayer(event.getGuild()).replaceLastSearches(event.getAuthor().getLongID(), results);

                    RequestBuffer.request(() -> {
                        event.getChannel().sendMessage(eb.build());
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    id++;
                }

            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
