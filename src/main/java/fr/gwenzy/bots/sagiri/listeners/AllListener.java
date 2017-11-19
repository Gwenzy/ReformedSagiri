package fr.gwenzy.bots.sagiri.listeners;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Created by gwend on 15/11/2017.
 */
public class AllListener extends SagiriListener implements IListener<Event> {
    public AllListener(boolean enabled){
        this.setEnabled(enabled);
    }

    @Override
    public void handle(Event event) {
        if(this.getEnabled()){
            if(!ReformedSagiri.logged.getOurUser().getPresence().getPlayingText().isPresent())
                ReformedSagiri.logged.changePlayingText(ReformedSagiri.cm.getCommand("help").getFullCommand());
        }
    }
}
