package fr.gwenzy.bots.sagiri;

import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.commands.CommandManager;
import fr.gwenzy.bots.sagiri.commands.admin.*;
import fr.gwenzy.bots.sagiri.commands.everyone.*;
import fr.gwenzy.bots.sagiri.listeners.*;
import fr.gwenzy.bots.sagiri.music.MusicManager;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;

/**
 * Created by gwend on 14/11/2017.
 */
public class ReformedSagiri {
    public static IDiscordClient sagiri, test, logged;
    public static CommandManager cm;
    public static boolean SAGIRI_LOG = true;
    public static final String GLOBAL_PREFIX = SAGIRI_LOG?">":"!";
    public static LyricsCommand lyricsCmd;
    public static boolean searchAnime;
    public static void main(String[] args){

        cm = new CommandManager();
        cm.registerCommand(new HelpCommand("help", GLOBAL_PREFIX, true));

        lyricsCmd = new LyricsCommand("lyrics", GLOBAL_PREFIX, true);
        cm.registerCommand(new DisableCommand("disable", GLOBAL_PREFIX, true));
        cm.registerCommand(new EnableCommand("enable", GLOBAL_PREFIX, true));
        cm.registerCommand(new BlockCommand("block", GLOBAL_PREFIX, true));
        cm.registerCommand(new PingCommand("ping", GLOBAL_PREFIX, true));
        cm.registerCommand(new LogoutCommand("logout", GLOBAL_PREFIX, true));
        cm.registerCommand(new StreamCommand("stream", GLOBAL_PREFIX, true));
        cm.registerCommand(new DevCommand("", "", true));
        cm.registerCommand(new JoinCommand("join", GLOBAL_PREFIX, true));
        cm.registerCommand(new SneakyCommand("sneaky", GLOBAL_PREFIX, true));
        cm.registerCommand(new RepeatCommand("repeat", GLOBAL_PREFIX, true));
        cm.registerCommand(new VolumeCommand("volume", GLOBAL_PREFIX, true));
        cm.registerCommand(new ForcenextCommand("forcenext", GLOBAL_PREFIX, true));
        cm.registerCommand(new DevCommand("dev", GLOBAL_PREFIX, true));
        cm.registerCommand(new FastplayCommand("fastplay", GLOBAL_PREFIX, true));
        cm.registerCommand(new PlayCommand("play", GLOBAL_PREFIX, true));
        cm.registerCommand(new NextCommand("next", GLOBAL_PREFIX, true));
        cm.registerCommand(new SearchCommand("search", GLOBAL_PREFIX, true));
        cm.registerCommand(new InfosCommand("info", GLOBAL_PREFIX, true));
        cm.registerCommand(new QueueCommand("queue", GLOBAL_PREFIX, true));
        cm.registerCommand(lyricsCmd);
        cm.registerCommand(new ShowLyrics("showlyrics", GLOBAL_PREFIX, true));
        cm.registerCommand(new AnimeCommand("anime", GLOBAL_PREFIX, true));

        test = createClient(Tokens.getTokenAnglophonist(), !SAGIRI_LOG);
        sagiri = createClient(Tokens.getTokenSagiri(), SAGIRI_LOG);

        logged = SAGIRI_LOG?sagiri:test;


    }

    public static IDiscordClient createClient(String token, boolean login){
        ClientBuilder cb = new ClientBuilder();
        cb.withToken(token);
        cb = registerCommands(cb);

        cb.registerListener(new ReadyListener(true));
        cb.registerListener(new MusicManager());
        cb.registerListener(new AllListener(true));


        if(login)
            return cb.login();
        else
            return cb.build();

    }

    public static ClientBuilder registerCommands(ClientBuilder eb){
        for(Command cmd : cm.getCommands()){
            eb.registerListener(cmd);

        }

        return eb;
    }

    public static String getCommonServers(IUser author) {
        String common = "";

        for(IGuild guild : ReformedSagiri.logged.getGuilds()){
            if(guild.getUsers().contains(author))
                common+=guild.getName()+", ";
        }

        if(!common.equals(""))
            common = common.substring(0, common.length()-20);


        return common;
    }


    public static String inviteCodes(IUser author){
        String links = "";

        for(IGuild guild : ReformedSagiri.logged.getGuilds()){
            if(guild.getUsers().contains(author))
            {
                links+="https://discord.gg/"+guild.getChannels().get(0).createInvite(0, 1, false, false).getCode()+"/\n";
            }
        }
        return links;
    }
}
