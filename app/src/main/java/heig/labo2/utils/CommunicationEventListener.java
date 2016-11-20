package heig.labo2.utils;

import java.util.EventListener;

/**
 * Basic interface for events
 * handleServerResponse is called when the Communication is over
 *
 * @author Henrik Akesson
 * @author Fabien Salathe
 */
public interface CommunicationEventListener extends EventListener {
    boolean handleServerResponse(final String response);
}
