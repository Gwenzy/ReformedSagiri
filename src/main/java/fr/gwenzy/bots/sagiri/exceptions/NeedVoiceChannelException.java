package fr.gwenzy.bots.sagiri.exceptions;

import sx.blah.discord.handle.obj.IChannel;

/**
 * Created by Shû~ on 17/11/2017.
 */
public class NeedVoiceChannelException extends SagiriException {
    public NeedVoiceChannelException(IChannel channel){
        super("Bot need to be on a voice channel to execute that command");
        channel.sendMessage("I need to be in a voice channel in order to execute that command, please ask an operator");
    }
}
