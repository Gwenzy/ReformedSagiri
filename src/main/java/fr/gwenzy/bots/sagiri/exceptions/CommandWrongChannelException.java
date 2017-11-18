package fr.gwenzy.bots.sagiri.exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by gwend on 14/11/2017.
 */
public class CommandWrongChannelException extends SagiriException {

    public CommandWrongChannelException(String name, IChannel channel){
        super(name+" command can't be used in this channel");
        channel.sendMessage(name+" command cannot be used in this channel");
    }
}
