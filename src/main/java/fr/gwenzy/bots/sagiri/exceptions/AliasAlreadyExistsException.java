package fr.gwenzy.bots.sagiri.exceptions;

/**
 * Created by gwend on 14/11/2017.
 */
public class AliasAlreadyExistsException extends SagiriException {

    public AliasAlreadyExistsException(String name, String alias){
        super("Alias "+alias+" already set for "+name+" command");
    }
}
