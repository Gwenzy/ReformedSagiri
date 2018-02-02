package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.AliasAlreadyExistsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shû~ on 01/02/2018.
 */
public class ShowLyrics extends Command {

    public ShowLyrics(String name, String prefix, boolean enabled) {

        super(name, prefix, enabled);
        this.addNeededArg("Search ID");
        this.addAuthorizedGroup("@everyone");
        this.setHelpContent("Shows Lyrics");
        this.addForcedChannel("408784079111323668");
        this.addForcedChannel("408784541747118100");
        try {
            this.addAlias("showlrc");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if (canBeExecuted(event)) {
                for (IMessage msg : event.getChannel().getFullMessageHistory().asArray()) {
                    while (RequestBuffer.getIncompleteRequestCount() > 0) {
                        Thread.sleep(1000);
                    }


                    RequestBuffer.request(() -> {
                        msg.delete();
                    });
                }

                int i = Integer.parseInt(getArgs(event.getMessage().getFormattedContent())[0]);

                event.getChannel().sendMessage(ReformedSagiri.lyricsCmd.lastSearch.get(i).getLyricURL());
                File f = new File("lrc.lrc");
                if(f.exists())f.delete();


                FileUtils.copyURLToFile(ReformedSagiri.searchAnime?new File(ReformedSagiri.lyricsCmd.lastSearch.get(i).getLyricURL()).toURI().toURL():new URL(ReformedSagiri.lyricsCmd.lastSearch.get(i).getLyricURL()), f);
                String txt = "";
                String txt2 = "";
                String txt3 = "";
                for(String l : Files.readAllLines(f.toPath())){

                    if(!ReformedSagiri.searchAnime || (l.startsWith("Comment: ") || l.startsWith("Dialogue: ") || l.startsWith("\n"))) {

                        String nl = l.replaceAll("\\[.*\\]", "").replaceAll("Dialogue: ", "").replaceAll("Comment :", "").replaceAll("\\{(.*?)\\}", "");
                        nl = nl.split(",")[nl.split(",").length-1];
                        nl+="\n";
                        if (txt.length() + (nl).length() <= 2000)
                            txt += nl;
                        else if (txt2.length() + (nl).length() <= 2000)
                            txt2 += nl;
                        else if (txt3.length() + (nl).length() <= 2000)
                            txt3 += nl;
                    }

                    System.out.println(txt);
                    System.out.println(txt2);
                    System.out.println(txt3);
                }
                event.getChannel().sendMessage("```"+txt+"```");
                event.getChannel().sendMessage(!txt2.equals("") ? "```" + txt2 + "```" : "");
                event.getChannel().sendMessage(!txt3.equals("") ? "```" + txt3 + "```" : "");
            }

        } catch (SagiriException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
