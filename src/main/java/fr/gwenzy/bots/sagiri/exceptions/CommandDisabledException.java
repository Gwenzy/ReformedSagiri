package fr.gwenzy.bots.sagiri.exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class CommandDisabledException extends SagiriException {
    public CommandDisabledException(IChannel channel){
        super("La commande a été désactivée");
        channel.sendMessage("Woops, this command has been disabled by the developer, try contacting him to know what's happening (see >help dev)");
    }
}
