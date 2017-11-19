package fr.gwenzy.bots.sagiri.commands.admin;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.MissingArgsException;
import fr.gwenzy.bots.sagiri.exceptions.SagiriException;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class BlockCommand extends Command{
    private static List<Long> blocked;

    public BlockCommand(String name, String prefix, boolean enabled) {
        super(name, prefix, enabled);
        this.addAuthorizedClient(Tokens.ID_DEVELOPER);
        this.addNeededArg("ID");
        this.setHelpContent("Blocks >dev command for someone");
        this.blocked = new ArrayList<>();
    }

    public static boolean isBlocked(IUser user){
        return blocked.contains(user.getLongID());
    }
    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                long ID = Long.parseLong(getArgs(event.getMessage().getFormattedContent())[0]);
                if(this.blocked.contains(ID)) {
                    this.blocked.remove(ID);
                    event.getChannel().sendMessage("User "+ID+" is now unblocked from dev command");
                }
                else{
                    this.blocked.add(ID);
                    event.getChannel().sendMessage("User "+ID+" is now blocked from dev command");
                }
            }
        } catch (SagiriException e) {
            e.printStackTrace();
        } catch (NumberFormatException e){
            try {
                throw new MissingArgsException(event.getChannel());
            } catch (MissingArgsException e1) {
                e1.printStackTrace();
            }
        }
    }
}
