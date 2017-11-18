package fr.gwenzy.bots.sagiri.listeners;

import fr.gwenzy.bots.sagiri.ReformedSagiri;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;

/**
 * Created by gwend on 15/11/2017.
 */
public class ReadyListener extends SagiriListener implements IListener<ReadyEvent> {
    public ReadyListener(boolean enabled){
        this.setEnabled(enabled);
    }

    @Override
    public void handle(ReadyEvent event) {
        if(this.getEnabled()){
            ReformedSagiri.logged.online();
            ReformedSagiri.logged.changePlayingText(ReformedSagiri.cm.getCommand("help").getFullCommand());
        }
    }
}
