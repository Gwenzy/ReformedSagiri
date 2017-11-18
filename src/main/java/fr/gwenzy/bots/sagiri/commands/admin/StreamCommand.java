package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.MissingArgsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IPresence;
import sx.blah.discord.handle.obj.StatusType;

/**
 * Created by Shû~ on 18/11/2017.
 */
public class StreamCommand extends Command {


    public StreamCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedClient(Tokens.ID_DEVELOPER);
        this.setHelpContent("Sets streaming mode: reserved to developer");
        this.addFacultativeArg("Twitch link");
        this.addFacultativeArg("Description");
    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                String[] args = getArgs(event.getMessage().getFormattedContent());
                if(args.length==0){
                    if(!ReformedSagiri.logged.getOurUser().getPresence().getStreamingUrl().isPresent()) {
                        ReformedSagiri.logged.streaming("My developer is improving me", "https://www.twitch.tv/thaksin_");
                        ReformedSagiri.logged.changePlayingText("My developer is improving me");
                        event.getChannel().sendMessage("Streaming mode enabled");
                    }
                    else {
                        ReformedSagiri.logged.online(ReformedSagiri.cm.getCommand("help").getFullCommand());
                        event.getChannel().sendMessage("Streaming mode disabled");
                    }
                }
                else if(args.length>=2){
                    ReformedSagiri.logged.streaming(getArgMessage(event.getMessage().getFormattedContent(), 1), args[0]);
                    ReformedSagiri.logged.changePlayingText(getArgMessage(event.getMessage().getFormattedContent(), 1));
                    event.getChannel().sendMessage("Custom streaming mode enabled");
                }
                else
                    throw new MissingArgsException(event.getChannel());
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }


}
