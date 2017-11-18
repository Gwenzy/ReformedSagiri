package fr.gwenzy.bots.sagiri.commands;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import fr.gwenzy.bots.sagiri.exceptions.*;
import fr.gwenzy.bots.sagiri.ressources.Tokens;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwend on 14/11/2017.
 */
public class Command implements IListener<MessageReceivedEvent>{
    private String name, prefix, help_title, help_content;
    private List<String> aliases, authorizedGroups, authorizedClients, forcedChannels, forbidenChannels, neededArgs, facultativeArgs;
    private boolean acceptPrivate, acceptChannel, needVoice, enabled;
    public Command(String name, String prefix, boolean enabled){
        this.enabled = enabled;
        this.name = name;
        this.prefix = prefix;
        this.help_title = prefix+name;
        this.help_content = "No Help Content";
        this.prefix = prefix;
        this.aliases = new ArrayList<>();
        this.authorizedClients = new ArrayList<>();
        this.authorizedGroups = new ArrayList<>();
        this.neededArgs = new ArrayList<>();
        this.facultativeArgs = new ArrayList<>();
        this.forcedChannels = new ArrayList<>();
        this.forbidenChannels = new ArrayList<>();
        this.acceptChannel = true;
        this.needVoice = false;
        this.acceptPrivate = false;
    }

    public void setAcceptPrivate(boolean accept){
        this.acceptPrivate = accept;
    }
    public void setNeedVoice(boolean voice){
        this.needVoice = voice;
    }

    public void setAcceptChannel(boolean accept){
        this.acceptChannel = accept;
    }
    public String[] getArgs(String command){
        while(command.contains("  ")){
            command = command.replaceAll("  ", " ");
        }
        String[] args = new String[command.split(" ").length-1];

        for(int i=0; i<args.length; i++){
            args[i] = command.split(" ")[i+1];
        }

        return args;
    }
    public void setHelp(String title, String content){
        this.help_title = title;
        this.help_content = content;
    }

    public void setHelpContent(String content){
        this.help_content = content;
    }

    public void setHelpTitle(String title){
        this.help_title = title;
    }

    public void addNeededArg(String argName){
        this.neededArgs.add(argName);
        this.setHelpTitle(this.getHelpTitle()+" <"+argName+">");
    }

    public void addFacultativeArg(String argName){
        this.facultativeArgs.add(argName);
        this.setHelpTitle(this.getHelpTitle()+" ["+argName+"]");
    }

    public String getArgMessage(String command, int startIndex){
        String[] args = getArgs(command);
        String argMessage="";
        for(int i =startIndex; i<args.length-1; i++){
            argMessage+=args[i]+" ";
        }
        argMessage+=args[args.length-1];
        return argMessage;
    }
    public void addAuthorizedGroup(String gpe) {
        this.authorizedGroups.add(gpe);
    }

    public void addAuthorizedClient(String client) {
        this.authorizedClients.add(client);
    }

    public String getHelpTitle(){
        return this.help_title;
    }

    public String getHelpContent(){
        return this.help_content;
    }

    public void addAlias(String alias) throws AliasAlreadyExistsException {
        if(!this.aliases.contains(alias))
            this.aliases.add(alias);
        else
            throw new AliasAlreadyExistsException(this.name, alias);
    }

    public boolean canBeExecuted(MessageReceivedEvent event) throws SagiriException {

        boolean prefixOk = false;
        boolean permsOk = false;
        boolean channelsOk = false;
        boolean argsOk = false;
        boolean voiceOk;
        boolean enabledOk = enabled;
        String message = event.getMessage().getFormattedContent();
        if(!enabled && event.getAuthor().getStringID().equals(Tokens.ID_DEVELOPER))
            enabledOk = true;
        if(message.toLowerCase().startsWith(this.prefix+this.name))
            prefixOk = true;
        for(String alias : aliases){
            if(message.toLowerCase().startsWith(this.prefix+alias))
                prefixOk = true;
        }

        if(prefixOk) {
            if (authorizedClients.contains(event.getAuthor().getStringID()))
                permsOk = true;
            if(!event.getChannel().isPrivate())
                for (String gpeID : authorizedGroups) {

                    for (IRole role : event.getAuthor().getRolesForGuild(event.getGuild())) {
                        if (gpeID.equals(role.getStringID())||gpeID.equalsIgnoreCase(role.getName()))
                            permsOk = true;
                    }
                }
            else
                permsOk = true;
            if ((forcedChannels.size() == 0 && !forbidenChannels.contains(event.getChannel().getStringID()) && (!event.getChannel().isPrivate() && this.acceptChannel)) || (event.getChannel().isPrivate() && this.acceptPrivate))
                channelsOk = true;
            else if((forcedChannels.contains(event.getChannel().getStringID()) && !forbidenChannels.contains(event.getChannel().getStringID()) && (!event.getChannel().isPrivate()) && this.acceptChannel) || (event.getChannel().isPrivate() && this.acceptPrivate))
                channelsOk = true;

            if(getArgs(event.getMessage().getFormattedContent()).length>=this.getNeededArgs().size())
                argsOk = true;
            if(!this.needVoice)
                voiceOk = true;
            else
                voiceOk = ReformedSagiri.logged.getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel()!=null;

            if(permsOk && channelsOk && argsOk && voiceOk && enabledOk)
                return true;

            else if(!enabledOk)
                throw new CommandDisabledException(event.getChannel());
            else if(!permsOk)
                throw new CommandMissingPermissionException(this.name, event.getChannel());
            else if(!channelsOk) {
                if((event.getChannel().isPrivate() && this.acceptPrivate) || (!event.getChannel().isPrivate() && this.acceptChannel))
                    throw new CommandWrongChannelException(this.name, event.getChannel());
            }
            else if(!argsOk)
                throw new MissingArgsException(event.getChannel());
            else if(!voiceOk)
                throw new NeedVoiceChannelException(event.getChannel());
        }
        return false;
    }

    @Override
    public void handle(MessageReceivedEvent messageReceivedEvent) {
        System.out.println("If you see this message, this command is probably currently in development, please try again later");
    }


    public List<String> getAliases() {
        return this.aliases;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAuthorizedClients() {
        return this.authorizedClients;
    }

    public List<String> getAuthorizedGroups() {
        return this.authorizedGroups;
    }

    public List<String> getForbiddenChannels() {
        return this.forbidenChannels;
    }

    public void addForbiddenChannel(String s) {
        this.forbidenChannels.add(s);
    }

    public void addForcedChannel(String s) {
        this.forcedChannels.add(s);
    }

    public List<String> getForcedChannels() {
        return this.forcedChannels;
    }

    public String getFullCommand() {
        return this.prefix+this.name;
    }

    public List<String> getNeededArgs() {
        return this.neededArgs;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }
}
