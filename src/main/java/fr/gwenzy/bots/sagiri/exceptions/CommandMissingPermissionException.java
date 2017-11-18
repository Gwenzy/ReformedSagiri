package fr.gwenzy.bots.sagiri.exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by gwend on 14/11/2017.
 */
public class CommandMissingPermissionException extends SagiriException {

    public CommandMissingPermissionException(String name, IChannel channel){
        super("More privileges are needed to use "+name+" command\n");
        channel.sendMessage("Vous n'avez pas les autorisations nécessaires pour exécuter la commande "+name);
    }
}
