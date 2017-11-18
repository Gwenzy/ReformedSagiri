package fr.gwenzy.bots.sagiri.exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by Shû~ on 16/11/2017.
 */
public class MissingArgsException extends SagiriException {

    public MissingArgsException(IChannel channel) {
        super("Args are missing for this command");
        channel.sendMessage("Some arguments are missing to execute properly this command, look at the help if you need some assistance");
    }
}
