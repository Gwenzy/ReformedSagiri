package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.commands.admin.BlockCommand;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

/**
 * Created by gwend on 15/11/2017.
 */
public class DevCommand extends Command {
    public DevCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.setHelpContent("You want to ask a question to Sagiri's developer ? This command is for you :p");
        this.addFacultativeArg("message");
        this.addAuthorizedGroup("@everyone");
        try {
            this.addAlias("contact");
            this.addAlias("developer");
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        }
        if(name.equals("")) {
            this.setAcceptChannel(false);
            this.setAcceptPrivate(true);
        }

    }

    @Override
    public void handle(MessageReceivedEvent event){
        try {
            if(canBeExecuted(event)){
                if(BlockCommand.isBlocked(event.getAuthor()))
                    throw new CommandMissingPermissionException(this.getName(), event.getChannel());

                if(getArgs(event.getMessage().getFormattedContent()).length==0) {
                    if (ReformedSagiri.getCommonServers(event.getAuthor()).equals(""))
                        event.getChannel().sendMessage("You must have at least 1 common server with me to send a message to the developer");
                    else {
                        ReformedSagiri.logged.getUserByID(Long.parseLong(Tokens.ID_DEVELOPER)).getOrCreatePMChannel().sendMessage(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + " sur le(s) serveur(s) " + ReformedSagiri.getCommonServers(event.getAuthor()) + " voudrait vous contacter, vous pouvez le retrouver ici : \n" +
                                ReformedSagiri.inviteCodes(event.getAuthor()));
                        event.getChannel().sendMessage("The message has been send to the developper !");
                    }
                }
                else{
                    if (ReformedSagiri.getCommonServers(event.getAuthor()).equals(""))
                        event.getChannel().sendMessage("You must have at least 1 common server with me to send a message to the developer");
                    else {
                        ReformedSagiri.logged.getUserByID(Long.parseLong(Tokens.ID_DEVELOPER)).getOrCreatePMChannel().sendMessage(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + " sur le(s) serveur(s) " + ReformedSagiri.getCommonServers(event.getAuthor()) + " voudrait vous contacter, vous pouvez le retrouver ici : \n" +
                                        ReformedSagiri.inviteCodes(event.getAuthor())
                        + "\n\n**"+(getName().equals("")?event.getMessage().getFormattedContent():getArgMessage(event.getMessage().getFormattedContent(), 0))+"**"

                        );
                        event.getChannel().sendMessage("The message has been sent to the developper !");
                    }
                }

            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}
