package fr.gwenzy.bots.sagiri.commands.everyone;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.commands.Command;
import fr.gwenzy.bots.sagiri.exceptions.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

/**
 * Created by gwend on 14/11/2017.
 */
public class HelpCommand extends Command {
    public HelpCommand(String name, String prefix, boolean enable) {
        super(name, prefix, enable);
        this.setHelpContent("Display this message or more help for a specified command");
        this.addAuthorizedGroup("@everyone");
        this.addFacultativeArg("command");

    }

    @Override
    public void handle(MessageReceivedEvent event) {
        try {
            if (canBeExecuted(event)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.withTitle("Bot help command");
                eb.withColor(Color.CYAN);

                if (getArgs(event.getMessage().getFormattedContent()).length != 0 && ReformedSagiri.cm.getTextCommands().contains(getArgs(event.getMessage().getFormattedContent())[0].replaceAll("!!", ""))) {
                    Command cmd = null;
                    for (Command c : ReformedSagiri.cm.getCommands()) {
                        String command = getArgs(event.getMessage().getFormattedContent())[0].replace("!!", "");

                        if (c.getName().equalsIgnoreCase(command) || c.getAliases().contains(command)) {
                            cmd = c;
                            break;
                        }
                    }

                    String content = cmd.getHelpContent() + "\n\n" + (cmd.getAliases().size() != 0 ? "Aliases:" : "No alias");
                    for (String alias : cmd.getAliases()) {
                        content += "\n" + cmd.getPrefix() + alias;
                    }
                    content += "\n\nAuthorized users : \n";
                    for (String client : cmd.getAuthorizedClients()) {
                        content += ReformedSagiri.logged.getUserByID(Long.parseLong(client)).getName() + "#" + ReformedSagiri.logged.getUserByID(Long.parseLong(client)).getDiscriminator() + "\n";

                    }
                    content += "\nAuthorized groups : \n";
                    for (String client : cmd.getAuthorizedGroups()) {
                        IRole role = null;
                        try {
                            long id = Long.parseLong(client);
                            role = event.getGuild().getRoleByID(id);
                        } catch (Exception e) {
                            try {
                                role = event.getGuild().getRolesByName(client).get(0);
                            } catch (Exception e1) {
                            }
                        }
                        if (role != null)
                            content += role.getName() + "\n";

                    }
                    content += "\nForbidden Channels : \n";
                    for (String channel : cmd.getForbiddenChannels()) {
                        try{
                            IChannel ch = event.getGuild().getChannelByID(Long.parseLong(channel));
                            content+=ch.mention();
                        }catch(Exception e){}
                    }
                    content += "\nForced Channels : \n";
                    for (String channel : cmd.getForcedChannels()) {
                        try{
                            IChannel ch = event.getGuild().getChannelByID(Long.parseLong(channel));
                            content+=ch.mention();
                        }catch(Exception e){}
                    }
                    eb.appendField(cmd.getHelpTitle()+(!cmd.getEnabled()?"♦DISABLED♦":""), content, false);


                } else
                    for (Command cmd : ReformedSagiri.cm.getCommands()) {

                        String content = cmd.getHelpContent() + "\n\n" + (cmd.getAliases().size() != 0 ? "Aliases:" : "No alias");
                        for (String alias : cmd.getAliases()) {
                            content += "\n" + cmd.getPrefix() + alias;
                        }
                        /*content+="\n\nAuthorized users : \n";
                        for(String client : cmd.getAuthorizedClients()){
                            content+=ArianaReformed.logged.getUserByID(Long.parseLong(client)).getName()+"#"+ArianaReformed.logged.getUserByID(Long.parseLong(client)).getDiscriminator()+"\n";

                        }
                        content+="\nAuthorized groups : \n";
                        for(String client : cmd.getAuthorizedGroups()){
                            IRole role = null;
                            try{
                                long id = Long.parseLong(client);
                                role = event.getGuild().getRoleByID(id);
                            }catch(Exception e){
                                try {
                                    role = event.getGuild().getRolesByName(client).get(0);
                                }catch(Exception e1){}
                            }
                            if(role!=null)
                                content+=role.getName()+"\n";

                        }*/
                        if(!(cmd.getPrefix()+cmd.getName()).equals(""))
                            eb.appendField(cmd.getHelpTitle()+(!cmd.getEnabled()?"♦DISABLED♦":""), content, false);

                    }

                event.getChannel().sendMessage(eb.build());

            }
        } catch (SagiriException e) {
            e.printStackTrace();
        }
    }
}