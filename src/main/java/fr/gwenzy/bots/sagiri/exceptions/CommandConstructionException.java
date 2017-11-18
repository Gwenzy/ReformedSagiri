package fr.gwenzy.bots.sagiri.exceptions;

/**
 * Created by gwend on 14/11/2017.
 */
public class CommandConstructionException extends SagiriException {

    public CommandConstructionException(String name){
        super("Error while attempting to create "+name+" command");
    }
}
