package fr.gwenzy.bots.sagiri.commands.everyone;

import com.pedrohlc.viewlyricsppensearcher.LyricInfo;
import com.pedrohlc.viewlyricsppensearcher.ViewLyricsSearcher;
import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.AliasAlreadyExistsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import org.eclipse.jetty.util.preventers.DriverManagerLeakPreventer;
import org.xml.sax.SAXException;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shû~ on 01/02/2018.
 */
public class AnimeCommand extends Command {

    public AnimeCommand(String name, String prefix, boolean enabled) {

        super(name, prefix, enabled);
        this.addNeededArg("Title/Artist");
        this.addAuthorizedGroup("@everyone");
        this.setHelpContent("Searches Lyrics matching Artist or Title in ANIME MUSIC DATABASE");
        this.addForcedChannel("408784079111323668");
        this.addForcedChannel("408784541747118100");
        try {
            this.addAlias("lrca");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                for(IMessage msg : event.getChannel().getFullMessageHistory().asArray()) {
                    while(RequestBuffer.getIncompleteRequestCount()>0){
                        Thread.sleep(1000);
                    }


                    RequestBuffer.request(() -> {
                        msg.delete();
                    });
                }
                String search = getArgMessage(event.getMessage().getFormattedContent(), 0);
                Class.forName("org.sqlite.JDBC");
                List<LyricInfo> resultsTitle = new ArrayList<>();


                try(Connection con = DriverManager.getConnection("jdbc:sqlite:lyrics.db");
                    Statement state = con.createStatement();){
                    ResultSet result = state.executeQuery("SELECT * FROM lyrics WHERE author LIKE '%"+search+"%'");

                    while(result.next()){
                        LyricInfo li = new LyricInfo();
                        li.setLyricURL("/home/pi/Discord/lyrics/"+result.getString("path"));
                        li.setMusicTitle(result.getString("title")+" ("+result.getString("language")+")");
                        li.setMusicArtist(result.getString("author"));
                        li.setMusicAlbum(result.getString("album"));
                        resultsTitle.add(li);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                int i=1;

                while(RequestBuffer.getIncompleteRequestCount()>0){
                    Thread.sleep(1000);
                }
                ReformedSagiri.searchAnime = true;
                ReformedSagiri.lyricsCmd.lastSearch = new HashMap<>();

                if(resultsTitle.size()==0) {
                    RequestBuffer.request(() -> {


                        event.getChannel().sendMessage(new EmbedBuilder()
                                .withTitle("Résultats de la recherche 'auteur'")
                                .withColor(Color.RED)
                                .appendField("No result", "Aucunes paroles n'ont été trouvées dans les recherches d'auteurs", false)
                                .build());
                    });

                } else{



                    for(LyricInfo li : resultsTitle){
                        ReformedSagiri.lyricsCmd.lastSearch.put(i, li);
                        final int iF = i;
                        if(iF<=10) {
                            while (RequestBuffer.getIncompleteRequestCount() > 0) {
                                Thread.sleep(1000);
                            }
                            RequestBuffer.request(() -> {
                                event.getChannel().sendMessage("------------------------------------------\nRésultat #" + iF);
                            });
                            Thread.sleep(500);
                            while (RequestBuffer.getIncompleteRequestCount() > 0) {
                                Thread.sleep(1000);
                            }
                            RequestBuffer.request(() -> {

                                event.getChannel().sendMessage(new EmbedBuilder()
                                        .withTitle("Résultats de la recherche de titres '" + search + "'")
                                        .withColor(Color.YELLOW)
                                        .appendField("Title", li.getMusicTitle(), true)
                                        .appendField("Anime", li.getMusicArtist() != null && li.getMusicArtist().length() != 0 ? li.getMusicArtist() : "Unknown", true)
                                        .appendField("Type", li.getMusicAlbum() != null
                                                && li.getMusicAlbum().length() != 0 ?
                                                li.getMusicAlbum() : "Unknown", true)
                                        .appendField("Durée", li.getMusicLenght() != null && li.getMusicLenght().length() != 0 ? li.getMusicLenght() : "Unknown", true)
                                        .appendField("Rate", li.getLyricRate()!=null&&li.getLyricRatesCount()!= null? li.getLyricRate()+" on "+li.getLyricRatesCount()+" rates":"Unknown", true)
                                        .build());


                            });
                            Thread.sleep(500);
                        }

                        if(iF<=10&&resultsTitle.size()!=0)
                            i++;
                    }


                }

                resultsTitle = new ArrayList<>();


                try(Connection con = DriverManager.getConnection("jdbc:sqlite:lyrics.db");
                    Statement state = con.createStatement();){
                    ResultSet result = state.executeQuery("SELECT * FROM lyrics WHERE title LIKE '%"+search+"%'");

                    while(result.next()){
                        LyricInfo li = new LyricInfo();
                        li.setLyricURL("/home/pi/Discord/lyrics/"+result.getString("path"));
                        li.setMusicTitle(result.getString("title")+" ("+result.getString("language")+")");
                        li.setMusicArtist(result.getString("author"));
                        li.setMusicAlbum(result.getString("album"));
                        resultsTitle.add(li);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                if(resultsTitle.size()==0) {

                    RequestBuffer.request(() -> {

                        event.getChannel().sendMessage(new EmbedBuilder()
                                .withTitle("Résultats de la recherche 'titre'")
                                .withColor(Color.RED)
                                .appendField("No result", "Aucunes paroles n'ont été trouvées dans les recherches de titres", false)
                                .build());
                    });

                } else{

                    int nbAut = i;



                    for(LyricInfo li : resultsTitle){
                        ReformedSagiri.lyricsCmd.lastSearch.put(i, li);

                        final int iF = i;
                        while(RequestBuffer.getIncompleteRequestCount()>0){
                            Thread.sleep(1000);
                        }
                        if(iF<10+nbAut) {
                            RequestBuffer.request(() -> {
                                event.getChannel().sendMessage("------------------------------------------\nRésultat #" + iF);

                            });

                            Thread.sleep(500);
                            while (RequestBuffer.getIncompleteRequestCount() > 0) {
                                Thread.sleep(1000);
                            }
                            RequestBuffer.request(() -> {

                                event.getChannel().sendMessage(new EmbedBuilder()
                                        .withTitle("Résultats de la recherche de titres '" + search + "'")
                                        .withColor(Color.GREEN)
                                        .appendField("Title", li.getMusicTitle(), true)
                                        .appendField("Anime", li.getMusicArtist() != null && li.getMusicArtist().length() != 0 ? li.getMusicArtist() : "Unknown", true)
                                        .appendField("Type", li.getMusicAlbum() != null
                                                && li.getMusicAlbum().length() != 0 ?
                                                li.getMusicAlbum() : "Unknown", true)
                                        .appendField("Durée", li.getMusicLenght() != null && li.getMusicLenght().length() != 0 ? li.getMusicLenght() : "Unknown", true)
                                        .appendField("Rate", li.getLyricRate()!=null&&li.getLyricRatesCount()!= null? li.getLyricRate()+" on "+li.getLyricRatesCount()+" rates":"Unknown", true)
                                        .build());

                            });
                            Thread.sleep(500);
                        }
                        i++;
                    }


                }

                while (RequestBuffer.getIncompleteRequestCount() > 0) {
                    Thread.sleep(1000);
                }
                RequestBuffer.request(()->event.getChannel().sendMessage("Fin de la recherche"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
